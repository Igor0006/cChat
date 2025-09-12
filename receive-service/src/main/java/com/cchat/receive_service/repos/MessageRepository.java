package com.cchat.receive_service.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cchat.receive_service.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        select m
        from Message m
        where m.conversation_id = :conversationId
        order by m.createdAt
        """)
    List<Message> getChatByConversationId(@Param("conversationId") Long conversationId);
}