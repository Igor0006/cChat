package com.cchat.receive_service.model;

import lombok.Data;

@Data
public class MessageDto {
    private String body;
    private Long senderId;
    private Long conversationId;
}