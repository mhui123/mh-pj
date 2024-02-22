package com.bandi.mhProject.component;

import com.bandi.mhProject.config.auth.PrincipalDetailsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    private PrincipalDetailsService userDetailService;
    private PasswordEncoder encoder;

    public CustomAuthenticationProvider(PrincipalDetailsService userDetailService, PasswordEncoder encoder) {
        this.userDetailService = userDetailService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetails user = userDetailService.loadUserByUsername(username);
        System.out.println("autenticate !!!!");
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
