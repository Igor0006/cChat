package com.cchat.ingress_service;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Controller {
    private final SenderService senderService;

    public Controller(SenderService senderService) {
        this.senderService = senderService;
    }

    @PostMapping("send/{reciever_id}")
    public void postMethodName(@RequestBody Message message, @RequestParam Long reciever_id) {
        senderService.sendMessage(message, reciever_id);
    }
    
}