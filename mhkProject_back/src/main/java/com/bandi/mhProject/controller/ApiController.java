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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, Object> data){
        String id = String.valueOf(data.get("username"));
        String pw = String.valueOf(data.get("password"));
        User user = User.builder().id(id).pw(pw).build();
        return impl.login(user);
    }
    @PostMapping("/logout")
    @ResponseBody
    public String logout(){
        System.out.println("logout");
        return "logout";
    }

    @PostMapping("/signin")
    @ResponseBody
    public Map<String, Object> signin(@RequestBody Map<String, Object> data){
        return impl.signin(data);
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
    public InfoDto getWordById(@RequestBody Map<String, Object> data){
        return wordImpl.getWordById(data);
    }

    @PostMapping("/getWordListByKeyword")
    @ResponseBody
    public Map<String, Object> getWordListByKeyword(@RequestBody Map<String, Object> data){
        return wordImpl.getWordListByKeyword(data);
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public Map<String, Object> changePassword(@RequestBody Map<String, Object> data){
        return impl.changePw(data);
    }

    @PostMapping("/getUserList")
    @ResponseBody
    public Map<String, Object> changePassword(){
        Map<String, Object> result = new HashMap<>();
        List<UserDto> userList = impl.getUserList();
        result.put("userList", userList);
        return result;
    }

    @PostMapping("/changeRole")
    @ResponseBody
    public Map<String, Object> changeRole(@RequestBody Map<String, Object> data){
        return impl.changeRole(data);
    }

    @PostMapping("/changeUseYn")
    @ResponseBody
    public Map<String, Object> changeUseYn(@RequestBody Map<String, Object> data){
        return impl.changeUseYn(data);
    }

    @PostMapping("/initializePw")
    @ResponseBody
    public Map<String, Object> initializePw(@RequestBody Map<String, Object> data){
        return impl.initializePw(data);
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/getAllWordList")
    @ResponseBody
    public Map<String, Object> getAllWordList(){
        Map<String, Object> result = new HashMap<>();
        List<InfoDto> list = wordImpl.getAllWordList();
        result.put("list", list);
        return result;
    }

    @PostMapping("/getMainWordList")
    @ResponseBody
    public Map<String, Object> getMainWordList(@RequestBody Map<String, Object> data){
        Map<String, Object> result = new HashMap<>();
        long pageIdx = Long.parseLong(String.valueOf(data.get("pageIdx")));
        List<InfoDto> list = wordImpl.getInfoListWithPage(pageIdx);
        result.put("list", list);
        return result;
    }

    @PostMapping("/getMyWordList")
    @ResponseBody
    public Map<String, Object> getMyWordList(@RequestBody Map<String, Object> data){
        return wordImpl.findMyInfoList(data);
    }

    @PostMapping("/delWords")
    @ResponseBody
    public Map<String, Object> delWords(@RequestBody Map<String, Object> data){
        return wordImpl.delWords(data);
    }

    @PostMapping("/getUserListByKeyword")
    @ResponseBody
    public Map<String, Object> getUserListByKeyword(@RequestBody Map<String, Object> data){
        return impl.getUserListByKeyword(data);
    }

    @PostMapping("/generateAuthKey")
    @ResponseBody
    public Map<String, Object> generateAuthKey(@RequestBody Map<String, Object> data) {
        return impl.generateAuthKey(data);
    }
    @PostMapping("/validateAuthKey")
    public Map<String, Object> validateAuthKey(@RequestBody Map<String, Object> data){
        return impl.validateAuthKey(data);
    }
}
