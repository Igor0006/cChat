package com.cchat.receive_service;

import org.springframework.web.bind.annotation.RestController;

import com.cchat.receive_service.model.Conversation;
import com.cchat.receive_service.model.ConversationDto;
import com.cchat.receive_service.model.Message;
import com.cchat.receive_service.services.ConversationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final ConversationService conversationService;
    
    @PostMapping("/createGroup/{groupName}")
    public void createGroup(@PathVariable String groupName, @RequestBody List<Long> userIds, @AuthenticationPrincipal Jwt jwt) {        
        conversationService.createGroup(groupName, userIds, jwt.getSubject());
    }

    private record MemberDto(Long conversationId, Long userId) {}

    @PreAuthorize("@conversationService.canAccessGroupEdit(#jwt.subject, #memberDto.conversationId)")
    @PostMapping("/addMember")
    public void addGroupMember(@RequestBody MemberDto memberDto,
                               @AuthenticationPrincipal Jwt jwt) {
        conversationService.addMember(memberDto.conversationId, memberDto.userId);
    }

    @PreAuthorize("@conversationService.canAccessGroupEdit(#jwt.subject, #memberDto.conversationId)")
    @PostMapping("/removeMember")
    public void removeGroupMember(@RequestBody MemberDto memberDto,
                                  @AuthenticationPrincipal Jwt jwt) {
        conversationService.removeMember(memberDto.conversationId, memberDto.userId);
    }
    
    @PostMapping("/addContact")
    public void addContact(@RequestBody String contactUsername, @AuthenticationPrincipal Jwt jwt) {
        conversationService.getConversationForDm(jwt.getSubject(), contactUsername);
    }
    
    @GetMapping("/me")
    public String getMe(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return username;
    }
    
    @GetMapping("/conversations")
    public List<ConversationDto> getConversations(@AuthenticationPrincipal Jwt jwt) {
        return conversationService.getConverstions(jwt.getSubject());
    }

    @PreAuthorize("@conversationService.canAccess(#jwt.subject, #conversation_id)")
    @GetMapping("/chat/{conversation_id}")
    public List<Message> getChat(@PathVariable Long conversation_id, @AuthenticationPrincipal Jwt jwt) {
        return conversationService.getMessages(conversation_id, jwt.getSubject());
    }
    
}
