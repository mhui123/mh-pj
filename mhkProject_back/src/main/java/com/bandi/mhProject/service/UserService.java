package com.bandi.mhProject.service;

import com.bandi.mhProject.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface UserService {
    UserDetails login(User user);
    boolean signin(User user);
}
