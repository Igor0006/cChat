package com.cchat.receive_service;

public record Message(
    Long sender,
    Long reciever,
    String message
) {} 
