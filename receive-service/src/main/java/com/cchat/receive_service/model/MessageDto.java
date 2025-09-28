package com.cchat.receive_service.model;

import lombok.Data;

@Data
public class MessageDto {
    private String body;
    private Long sender_id;
    private Long conversation_id;
}