package com.cchat.ingress_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderService {
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final static Logger log = LoggerFactory.getLogger(SenderService.class);

    public SenderService(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Message message, String reciever_id) {
        log.info("Message from {} was sent to {}", message.sender(), reciever_id);
        kafkaTemplate.send("messages", reciever_id, message);
    }
}
