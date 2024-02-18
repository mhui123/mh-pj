package com.bandi.mhProject.controller;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    public ApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private final RestTemplate restTemplate;
    @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody Map<String ,Object> obj){
        System.out.println("[test] obj : "+obj);
//        return restTemplate.getForObject("hahaha", String.class);
        return "hahaha";
    }
}
