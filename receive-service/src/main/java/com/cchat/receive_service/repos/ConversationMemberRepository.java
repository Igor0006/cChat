package com.cchat.receive_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cchat.receive_service.model.ConversationMember;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {
    boolean existsByConversation_IdAndUserId(Long conversationId, Long userId);
}

