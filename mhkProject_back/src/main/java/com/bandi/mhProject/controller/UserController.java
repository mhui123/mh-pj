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
//@RequestMapping("/user")
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
    
    @PostMapping("/signin")
    @ResponseBody
    public String signin(@RequestBody User user){
        System.out.println("signin!!!!!!!!!!!!");
        boolean result = impl.signin(user);
        return "signin";
    }
    @PostMapping("/login")
    @ResponseBody
    public boolean login(@RequestBody User user){
        System.out.println("login!!!!!!!!!!!!");
        return impl.login(user);
    }

//    @PostConstruct
//    public void init(){
//        impl.signin(new User("admin", "admin01!", "admin"));
//    }
}
