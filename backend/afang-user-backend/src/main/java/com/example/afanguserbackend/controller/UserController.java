package com.example.afanguserbackend.controller;

import com.example.afanguserbackend.model.entity.user.Users;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/user")
public class UserController {

    @PostMapping("/test")
    public String test(){
        Users user = new Users();
        return "hello";
    }
}

