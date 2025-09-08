package com.cchat.receive_service.repos;

import com.cchat.receive_service.model.Conversation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
        select cm.conversation.id
        from ConversationMember cm
        where cm.conversation.type = com.cchat.receive_service.model.ConversationType.DM
          and cm.userId in (:a, :b)
        group by cm.conversation.id
        having count(distinct cm.userId) = 2
        """)
    Optional<Long> findDmConversationId(@Param("a") Long userA, @Param("b") Long userB);
}
