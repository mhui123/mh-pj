package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.config.auth.PrincipalDetail;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.UserService;
import com.bandi.mhProject.util.Encoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    @PersistenceContext
    EntityManager em;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    public UserDto login(User user) {
        String id = user.getId();
        String pw = user.getPw();
        User foundUser = userRepo.findByid(id);
        UserDto dto = UserDto.builder().build();
        if(foundUser != null){
//            return encoder.matchPwWithCompare(pw, foundUser.getPw());
            boolean result = encoder.matches(pw, foundUser.getPw());
            if(result){
                id = foundUser.getId();
                String role = foundUser.getRole();
                List<Info> infos = foundUser.getInfos();
                dto = UserDto.builder().id(id).role(role).infos(infos).authorized(true).state(200L).build();
                return dto;
            }
            else {
                //500 : PASSWORD NOT MATCHED
                dto = UserDto.builder().state(500L).build();
            }
        }
        return dto;
    }

    @Override
    public boolean signin(User user) {
        try {
            String id = user.getId();
            String pw = user.getPw(); //asdfasdfasdf
            String encoderedPw = encoder.encode(pw);
            String role = user.getRole() == null ? "ROLE_USER" : user.getRole();
//            User user1 = new User(id, encoderedPw, role);
            User user1 = User.builder()
                    .id(id).pw(encoderedPw).role(role)
                    .build();
            em.persist(user1);
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
