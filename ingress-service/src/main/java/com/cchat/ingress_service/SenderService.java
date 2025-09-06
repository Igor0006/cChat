package com.cchat.ingress_service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cchat.ingress_service.model.MessageDto;
import com.cchat.ingress_service.model.MessageRequest;

@Service
public class SenderService {
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;
    private final static Logger log = LoggerFactory.getLogger(SenderService.class);

    public SenderService(KafkaTemplate<String, MessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }    

    private long makeDirectConversationId(long u1, long u2) {
        return Objects.hash(u1, u2);
    }
    public void sendMessage(MessageRequest request, Long reciever_id) {
        log.info("Message from {} was sent to {}", request.senderId(), reciever_id);
        MessageDto message = new MessageDto(request.body(), request.senderId(), makeDirectConversationId(request.senderId()
        , reciever_id));
        kafkaTemplate.send("messages", Long.toString(reciever_id), message);
    }
}
