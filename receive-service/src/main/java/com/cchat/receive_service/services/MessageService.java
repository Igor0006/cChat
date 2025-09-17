package com.cchat.receive_service.services;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cchat.receive_service.model.Conversation;
import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.model.MessageDto;
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
    private final ConversationService converstaionService;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final SimpMessagingTemplate messaging;

    @KafkaListener(topics = "messages")
    @Transactional
    public void recieveMessage(ConsumerRecord<String, MessageDto> record) {
        MessageDto message = record.value();
        Long senderId = message.getSenderId();
        Long destinationId = message.getDestinationId();

        Conversation conversation;
        if (message.getType().isDm()) {
            conversation = converstaionService.getConversationForDm(senderId, destinationId);
        } else {
            conversation = conversationRepository.findById(destinationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found for id: " + destinationId));
        }
        // save message in db
        Message m = new Message();
        m.setConversation_id(conversation.getId());
        m.setSender_id(senderId);
        m.setBody(message.getBody());

        messageRepository.save(m);
        log.info("Saved message id={} conv={} sender={}", m.getId(), conversation.getId(), m.getSender_id());

        // set conversation unread for recievers
        conversationMemberRepository.markReadForUser(conversation.getId(), senderId);

        // raw sender to client via stomp
        var payload = Map.of(
            "id", m.getId(),
            "conversationId", m.getConversation_id(),
            "senderId", m.getSender_id(),
            "body", m.getBody()
        );
        messaging.convertAndSend("/topic/messages", payload);
    }

}
