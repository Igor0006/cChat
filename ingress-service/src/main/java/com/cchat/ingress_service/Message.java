package com.cchat.ingress_service;

public record Message(
    Long sender,
    Long reciever,
    String message
) {} 
