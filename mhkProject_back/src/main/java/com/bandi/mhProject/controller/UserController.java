package com.bandi.mhProject.controller;


import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.serviceimpl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl impl;

    @ResponseBody
    @PostMapping("/test")
    public Map<String, Object> test(){
       Map<String, Object> dummy = new HashMap<>();
       dummy.put("sample", "hahaha");
       return dummy;
    }
}
