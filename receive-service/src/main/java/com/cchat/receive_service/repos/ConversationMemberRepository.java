package com.cchat.receive_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cchat.receive_service.model.ConversationMember;


@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {
    boolean existsByConversation_IdAndUser_Id(Long conversationId, Long userId);
    @Modifying
    void deleteByConversation_IdAndUser_Id(Long conversationId, Long userId);
    @Modifying
    @Query("""
        update ConversationMember cm
        set cm.unread = true
        where cm.conversation.id = :conversationId
        and cm.user.id <> :senderId
        """)
    void markUnreadForOthers(Long conversationId, Long senderId);

    @Modifying
    @Query("""
        update ConversationMember cm
        set cm.unread = false
        where cm.conversation.id = :conversationId
        and cm.user.id = :userId
    """)
    void markReadForUser(Long conversationId, Long userId);
}

