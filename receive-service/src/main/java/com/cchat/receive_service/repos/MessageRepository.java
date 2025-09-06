package com.cchat.receive_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cchat.receive_service.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
