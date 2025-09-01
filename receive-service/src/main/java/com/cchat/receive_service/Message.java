package com.cchat.receive_service;

public record Message(
    Long sender,
    String message
) {} 
