package com.cchat.ingress_service.model;

import lombok.Data;

@Data
public class MessageDto {
    private String body;
    private Long senderId;
    private Long conversationId;
    private Long recieverId;

    public MessageDto(String body, Long senderId, Long conversationId) {
        this.body = body;
        this.senderId = senderId;
        this.conversationId = conversationId;
    }
}