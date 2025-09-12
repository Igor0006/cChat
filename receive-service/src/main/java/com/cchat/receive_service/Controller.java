package com.cchat.receive_service;

import org.springframework.web.bind.annotation.RestController;

import com.cchat.receive_service.model.User;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@Slf4j
public class Controller {
    
    @PostMapping("/createGroup/{groupName}")
    public void postMethodName(@PathVariable String groupName) {        
        
    }

    @GetMapping("/me")
    public String getMe(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        log.info(username);
        return username;
    }
    
    
}
