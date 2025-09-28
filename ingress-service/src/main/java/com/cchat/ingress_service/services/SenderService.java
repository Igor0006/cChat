package com.cchat.ingress_service.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cchat.ingress_service.model.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SenderService {
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    public void sendMessage(MessageDto message) {
        log.info("Message from {} was sent to {}", message.getSender_id(), message.getConversation_id());
        kafkaTemplate.send("messages", Long.toString(message.getConversation_id()), message);
    }
}
