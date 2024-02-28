package com.bandi.mhProject.service;

import com.bandi.mhProject.dto.JwtToken;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String ,Object> login(User user);
    Map<String ,Object> signin(User user);

    Map<String ,Object> changePw(Map<String, Object> data);
    List<UserDto> getUserList();
    Map<String, Object> changeRole(Map<String, Object> data);
    Map<String, Object> changeUseYn(Map<String, Object> data);

    Map<String, Object> initializePw(Map<String, Object> data);
    Map<String, Object> getUserListByKeyword(Map<String, Object> data);

    Map<String, Object> generateAuthKey(Map<String, Object> data);
}
