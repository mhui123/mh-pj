package com.bandi.mhProject.config.auth;

import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(" 자격증명 !!!!!!!!!");
        User entity = userRepo.findByid(username);
        if(entity != null){
            return new PrincipalDetail(entity);
        }
        return null;
    }
}
