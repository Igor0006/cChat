package com.cchat.receive_service.services;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cchat.receive_service.model.Conversation;
import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.model.MessageDto;
import com.cchat.receive_service.model.User;
import com.cchat.receive_service.repos.ConversationMemberRepository;
import com.cchat.receive_service.repos.ConversationRepository;
import com.cchat.receive_service.repos.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final SimpMessagingTemplate messaging;

    @KafkaListener(topics = "messages")
    @Transactional
    public void recieveMessage(ConsumerRecord<String, MessageDto> record) {
        MessageDto message = record.value();
        Long senderId = message.getSender_id();
        Long destinationId = message.getConversation_id();

        Conversation conversation;
        conversation = conversationRepository.findById(destinationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found for id: " + destinationId));
            
        // save message in db
        Message m = new Message();
        m.setConversation_id(conversation.getId());
        m.setSender_id(senderId);
        m.setBody(message.getBody());

        messageRepository.save(m);
        log.info("Saved message id={} conv={} sender={}", m.getId(), conversation.getId(), m.getSender_id());

        // set conversation unread for recievers
        conversationMemberRepository.markUnreadForOthers(conversation.getId(), senderId);

        // raw sender to client via stomp
        var payload = Map.of(
            "id", m.getId(),
            "conversation_id", m.getConversation_id(),
            "sender_id", m.getSender_id(),
            "body", m.getBody(),
            "createdAt", m.getCreatedAt()
        );
        
        messaging.convertAndSend("/topic/ping", Map.of("ok", true));
        
        for (User receiver: conversationMemberRepository.findOtherUserOfConversationIds(destinationId, senderId)) {
         log.info("toUser={}", receiver.getLogin());
        messaging.convertAndSendToUser(receiver.getLogin(), "/queue/update", payload);
        messaging.convertAndSendToUser(receiver.getLogin(), "/queue/messages" + conversation.getId(), payload);   
        }
    }

}
