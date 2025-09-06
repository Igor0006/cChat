package com.cchat.ingress_service;

import lombok.Data;

@Data
public class MessageDto {
    private String body;
    private Long senderId;
    private Long conversationId;
}