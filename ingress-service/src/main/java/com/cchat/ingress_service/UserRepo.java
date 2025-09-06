package com.cchat.ingress_service;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cchat.ingress_service.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
