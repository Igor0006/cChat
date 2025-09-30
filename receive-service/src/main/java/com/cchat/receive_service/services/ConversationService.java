package com.cchat.receive_service.services;

import com.cchat.receive_service.repos.ConversationMemberRepository;
import com.cchat.receive_service.repos.ConversationRepository;
import com.cchat.receive_service.repos.MessageRepository;
import com.cchat.receive_service.repos.UserRepository;
import com.cchat.receive_service.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public boolean canAccess(String login, Long conversationId) {
        if (memberRepository.existsByConversation_IdAndUser_Id(conversationId, userRepository.findByLogin(login))) {
            return true;
        }
        log.info("Access of user {} for conversation {} was denied", login, conversationId);
        return false;
    }

    public boolean canAccessGroupEdit(String login, Long conversationId) {
        if (conversationRepository.existsByIdAndOwnerId(conversationId, userRepository.findByLogin(login))) {
            return true;
        }
        log.info("Access of user {} for conversation {} edit was denied", login, conversationId);
        return false;
    }

    public List<ConversationDto> getConverstions(String login) {
        Long userId = userRepository.findByLogin(login);
        List<Conversation> list = conversationRepository.findConversations(userId);
        List<ConversationDto> dtoList = new ArrayList<>();
        for (var conv: list) {
            if (conv.getType().isDm()) {
                if (conv.getType().isDm()) {
                    String receiverLogin = memberRepository
                            .findOtherUserLogin(conv.getId(), userId)
                            .orElse(login); // fallback just in case
                    conv.setTitle(receiverLogin);
                }
            }
            dtoList.add(new ConversationDto(conv.getTitle(), conv.getId(), memberRepository.isUnread(conv.getId(), userId)));
        }
        return dtoList;
    }

    @Transactional
    public List<Message> getMessages(Long conversation_id, String username) {
        // set conversation as read
        memberRepository.markReadForUser(conversation_id, userRepository.findByLogin(username));
        return messageRepository.getChatByConversationId(conversation_id);
    }

    @Transactional
    public Conversation getConversationForDm(String user, String contact) {
        Long userAId = userRepository.findByLogin(user);
        Long userBId = userRepository.findByLogin(contact);

        var existingId = conversationRepository.findDmConversationId(userAId, userBId);
        if (existingId.isPresent()) {
            return conversationRepository.getReferenceById(existingId.get());
        }

        var conv = new Conversation();
        conv.setType(ConversationType.DM);
        conv = conversationRepository.save(conv);

        var userA = userRepository.getReferenceById(userAId);
        var userB = userRepository.getReferenceById(userBId);

        var m1 = new ConversationMember();
        m1.setConversation(conv);
        m1.setUser(userA);

        var m2 = new ConversationMember();
        m2.setConversation(conv);
        m2.setUser(userB);

        memberRepository.save(m1);
        memberRepository.save(m2);

        return conv;
    }

    @Transactional
    public Conversation createGroup(String title, Collection<Long> memberIds, String ownerUsername) {
        var conv = new Conversation();
        conv.setType(ConversationType.GROUP);
        conv.setTitle(title);
        conv.setOwnerId(userRepository.findByLogin(ownerUsername));
        conv = conversationRepository.save(conv);

        memberIds.add(userRepository.findByLogin(ownerUsername));
        for (Long uid : new HashSet<>(memberIds)) {
            var userRef = userRepository.getReferenceById(uid);
            var cm = new ConversationMember();
            cm.setConversation(conv);
            cm.setUser(userRef);
            memberRepository.save(cm);
        }
        return conv;
    }

    @Transactional
    public void addMember(Long conversationId, Long userId) {
        if (!memberRepository.existsByConversation_IdAndUser_Id(conversationId, userId)) {
            var convRef = conversationRepository.getReferenceById(conversationId);
            var userRef = userRepository.getReferenceById(userId);
            var cm = new ConversationMember();
            cm.setConversation(convRef);
            cm.setUser(userRef);
            memberRepository.save(cm);
        }
    }

    @Transactional
    public void removeMember(Long conversationId, Long userId) {
        memberRepository.deleteByConversation_IdAndUser_Id(conversationId, userId);
    }
    
    public void markAsRead(Long conversationId, String login) {
        memberRepository.markReadForUser(conversationId, userRepository.findByLogin(login));
    }
}
