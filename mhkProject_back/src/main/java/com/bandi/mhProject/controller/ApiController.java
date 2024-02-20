package com.bandi.mhProject.controller;

import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.serviceimpl.UserServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private UserServiceImpl impl;
    public ApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private final RestTemplate restTemplate;
    @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody Map<String, Object> obj){
//        return restTemplate.getForObject("hahaha", String.class);
        String id = String.valueOf(obj.get("username"));
        String pw = String.valueOf(obj.get("password"));
        User user = User.builder().id(id).pw(pw).role("ROLE_USER").build();
        System.out.println(user);
        impl.signin(user);
        return "hahaha";
    }
}
