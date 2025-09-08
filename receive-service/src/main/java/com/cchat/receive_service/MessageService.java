package com.cchat.receive_service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cchat.receive_service.model.Conversation;
import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.model.MessageDto;
import com.cchat.receive_service.repos.ConversationRepository;
import com.cchat.receive_service.repos.MessageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationService converstaionService;
    private final ConversationRepository conversationRepository;

    @KafkaListener(topics = "messages")
    @Transactional
    public void recieveMessage(ConsumerRecord<String, MessageDto> record) {
        MessageDto message = record.value();


        Message m = new Message();
        Conversation conversation;
        if (message.getType().isDm()) {
            conversation = converstaionService.getConversationForDm(message.getSenderId(), message.getDestinationId());
        } else {
            conversation = conversationRepository.findById(message.getDestinationId())
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found for id: " + message.getDestinationId()));
        }
        
        m.setConversation_id(conversation.getId());
        m.setSender_id(message.getSenderId());
        m.setBody(message.getBody());

        messageRepository.save(m);
        log.info("Saved message id={} conv={} sender={}", m.getId(), conversation.getId(), m.getSender_id());
    }

}
