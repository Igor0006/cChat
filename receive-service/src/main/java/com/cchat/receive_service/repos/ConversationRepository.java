package com.cchat.receive_service.repos;

import com.cchat.receive_service.model.Conversation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
        select c.id
        from Conversation c
        where c.type = com.cchat.receive_service.model.ConversationType.DM
          and exists (
            select 1 from ConversationMember m1
            where m1.conversation = c and m1.user.id = :a
          )
          and exists (
            select 1 from ConversationMember m2
            where m2.conversation = c and m2.user.id = :b
          )
        """)
    Optional<Long> findDmConversationId(@Param("a") Long userA, @Param("b") Long userB);
}
