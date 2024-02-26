package com.bandi.mhProject.service;

import com.bandi.mhProject.dto.JwtToken;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface UserService {
    UserDto login(User user);
    UserDto signin(User user);
}
