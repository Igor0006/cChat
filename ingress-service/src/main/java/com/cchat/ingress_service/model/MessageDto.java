package com.cchat.ingress_service.model;

import lombok.Data;

@Data
public class MessageDto {
    private String body;
    private Long senderId;
    private Long destinationId;
    private String type;
}