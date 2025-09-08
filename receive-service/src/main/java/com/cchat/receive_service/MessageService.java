package com.cchat.receive_service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.model.MessageDto;
import com.cchat.receive_service.repos.ConversationMemberRepository;
import com.cchat.receive_service.repos.MessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationMemberRepository memberRepository;

    @KafkaListener(topics = "messages")
    public void recieveMessage(ConsumerRecord<String, MessageDto> record) {
        MessageDto e = record.value();

        boolean member = memberRepository.existsByConversation_IdAndUser_Id(e.getConversationId(), e.getSenderId());
        if (!member) {
            log.warn("Skip: sender {} not in conversation {}", e.getSenderId(), e.getConversationId());
            return;
        }

        Message m = new Message();
        m.setConversation_id(e.getConversationId());
        m.setSender_id(e.getSenderId());
        m.setBody(e.getBody());

        messageRepository.save(m);
        log.info("Saved message id={} conv={} sender={}", m.getId(), m.getConversation_id(), m.getSender_id());
    }

}
