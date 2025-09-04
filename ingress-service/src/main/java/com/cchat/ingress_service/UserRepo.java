package com.cchat.ingress_service;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cchat.ingress_service.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
