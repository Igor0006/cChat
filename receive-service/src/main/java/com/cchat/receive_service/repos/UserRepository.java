package com.cchat.receive_service.repos;

import com.cchat.receive_service.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { 
    @Query("select u.id from User u where u.login = :login")
    Optional<Long> findByLogin(@Param("login") String login);
}
