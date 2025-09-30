package com.cchat.receive_service;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.cchat.receive_service.services.ConversationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StompConroller {
    private final ConversationService convService;
    
    @MessageMapping("/chat/{conversationId}/read")
    public void markAsRead(@DestinationVariable Long conversationId, Principal principal) {
        convService.markAsRead(conversationId, principal.getName());
        log.info("Mark conversation {} as read for user {}", conversationId, principal.getName());
    }
}
