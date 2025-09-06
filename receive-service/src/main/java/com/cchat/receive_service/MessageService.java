package com.cchat.receive_service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Service;

import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.model.MessageDto;
import com.cchat.receive_service.repos.MessageRepository;;

@Service
public class MessageService {
    private final ConcurrentKafkaListenerContainerFactory containerFactory;
    private final MessageRepository messagerepository;
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    
    public MessageService(ConcurrentKafkaListenerContainerFactory containerFactory, MessageRepository messageRepository) {
        this.containerFactory = containerFactory;
        this.messagerepository = messageRepository;
    }

    @KafkaListener(topics = "messages")
    public void recieveMessage(ConsumerRecord<String, MessageDto> record) {
        Message message = new Message();
        message.setSender_id(record.value().getSenderId());
        message.setBody(record.value().getBody());
        message.setConversation_id(record.value().getConversationId());
        messagerepository.save(message);
        log.info("Message from user {} recieved. Reciever: {}", record.value().getConversationId(), record.key());

    }

}
