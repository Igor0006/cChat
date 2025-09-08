package com.cchat.ingress_service;

import org.springframework.web.bind.annotation.RestController;

import com.cchat.ingress_service.model.MessageDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class Controller {
    private final SenderService senderService;

    public Controller(SenderService senderService) {
        this.senderService = senderService;
    }

    @PostMapping("send")
    public void postMethodName(@RequestBody MessageDto message) {
        senderService.sendMessage(message);
    }
    
}