package com.bandi.mhProject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Encoder {
    private final PasswordEncoder ec;

    public String encryptPw(String pw){
        return ec.encode(pw);
    }

    public boolean matchPwWithCompare(String pw, String compare){
        return ec.matches(pw, compare);
    }
}
