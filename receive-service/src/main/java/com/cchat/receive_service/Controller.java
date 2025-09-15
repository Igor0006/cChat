package com.cchat.receive_service;

import org.springframework.web.bind.annotation.RestController;

import com.cchat.receive_service.model.Conversation;
import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.model.User;
import com.cchat.receive_service.services.ConversationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final ConversationService conversationService;
    
    @PostMapping("/createGroup/{groupName}")
    public void postMethodName(@PathVariable String groupName) {        
        
    }

    @GetMapping("/me")
    public String getMe(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return username;
    }
    
    @GetMapping("/conversations")
    public List<Conversation> getConversations(@AuthenticationPrincipal Jwt jwt) {
        return conversationService.getConverstions(jwt.getSubject());
    }

    @PreAuthorize("@conversationService.canAccess(#jwt.subject, #conversation_id)")
    @GetMapping("/chat/{conversation_id}")
    public List<Message> getChat(@PathVariable Long conversation_id, @AuthenticationPrincipal Jwt jwt) {
        return conversationService.getMessages(conversation_id);
    }
    
}
