package com.bandi.mhProject.controller;

import com.bandi.mhProject.dto.InfoDto;
import com.bandi.mhProject.dto.JwtToken;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.serviceimpl.UserServiceImpl;
import com.bandi.mhProject.serviceimpl.WordServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private UserServiceImpl impl;
    @Autowired
    private WordServiceImpl wordImpl;
    @Autowired
    private final RestTemplate restTemplate;
    public ApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


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

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<JwtToken> login(@RequestBody Map<String, Object> data){
        System.out.println("login ");
        String id = String.valueOf(data.get("username"));
        String pw = String.valueOf(data.get("password"));
        User user = User.builder().id(id).pw(pw).build();
        JwtToken token = impl.login(user);
//        UserDto result = impl.login(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    @ResponseBody
    public UserDto signup(@RequestBody Map<String, Object> data){
        System.out.println("signin : " + data);
        String id = String.valueOf(data.get("id"));
        String pw = String.valueOf(data.get("pw"));
        User user = User.builder().id(id).pw(pw).build();
        UserDto result = impl.signin(user);
        return result;
    }

    @PostMapping("/addWord")
    @ResponseBody
    public Map<String, Object> addWord(@RequestBody Map<String, Object> data){
        Map<String, Object> result = wordImpl.addWord(data);

        return result;
    }

    @PostMapping("/getList")
    @ResponseBody
    public List<InfoDto> getList(){
        return wordImpl.getInfoList();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> deleteWord(@PathVariable String id){
        return wordImpl.deleteWord(id);
    }

    @PutMapping("/edit")
    @ResponseBody
    public Map<String, Object> editWord(@RequestBody Map<String, Object> data){
        System.out.println(data);
        return wordImpl.editWord(data);
//        return new HashMap<>();
    }

    @PostMapping("/getWordById")
    @ResponseBody
    public InfoDto getWordById(@RequestParam String id){
        return wordImpl.getWordById(id);
    }

    @PostMapping("/getWordListByKeyword")
    @ResponseBody
    public List<InfoDto> getWordListByKeyword(@RequestParam String keyword){
        return wordImpl.getWordList(keyword);
    }
}
