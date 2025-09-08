package com.cchat.ingress_service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.info("Message from {} was sent to {}", message.getSenderId(), message.getDestinationId());
        kafkaTemplate.send("messages", Long.toString(message.getDestinationId()), message);
    }
}
