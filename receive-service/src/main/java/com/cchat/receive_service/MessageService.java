package com.cchat.receive_service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final ConcurrentKafkaListenerContainerFactory containerFactory;
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    
    public MessageService(ConcurrentKafkaListenerContainerFactory containerFactory) {
        this.containerFactory = containerFactory;
    }

    @KafkaListener(topics = "messages")
    public void recieveMessage(ConsumerRecord<String, Message> record) {
        log.info("Message from user {} recieved. Reciever: {}", record.value().sender(), record.key());
    }

}
