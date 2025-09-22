package com.cchat.ingress_service;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cchat.ingress_service.model.User;


public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Query("select u.id from User u where u.login = :login")
    Long findIdByLogin(String login);
}
