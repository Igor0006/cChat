package com.cchat.receive_service;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class Controller {
    
    @PostMapping("/createGroup/{groupName}")
    public void postMethodName(@PathVariable String groupName) {        
        
    }
    
}
