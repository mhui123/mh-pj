package com.bandi.mhProject.service;

import com.bandi.mhProject.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    boolean login(User user);
    boolean signin(User user);
}
