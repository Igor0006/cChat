package com.cchat.ingress_service.model;

public record MessageRequest(
    String body,
    Long senderId
) { }
