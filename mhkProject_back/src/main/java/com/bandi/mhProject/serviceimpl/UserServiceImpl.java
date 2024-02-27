package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.component.JwtTokenProvider;
import com.bandi.mhProject.config.auth.PrincipalDetail;
import com.bandi.mhProject.dto.JwtToken;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.UserService;
import com.bandi.mhProject.util.Encoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final AuthenticationManagerBuilder authBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired private AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepo, AuthenticationManagerBuilder authBuilder, JwtTokenProvider jwtTokenProvider, PasswordEncoder encoder
    ) {
        this.userRepo = userRepo;
        this.authBuilder = authBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.encoder = encoder;
    }

    @PersistenceContext
    EntityManager em;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    public UserDto login(User user) {
        String id = user.getId();
        String pw = user.getPw();
        User foundUser = userRepo.findByid(id);
        JwtToken token = null;

        UserDto dto = UserDto.builder().build();
        if(foundUser != null){
//            return encoder.matchPwWithCompare(pw, foundUser.getPw());
            boolean result = encoder.matches(pw, foundUser.getPw());
            if(result){
                id = foundUser.getId();
                String role = foundUser.getRole();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, pw);
                Authentication authentication = authBuilder.getObject().authenticate(authToken); //authenticationManager.authenticate(authToken);
                token = jwtTokenProvider.generateToken(authentication);
                dto = UserDto.builder().id(id).role(role).authorized(true).state(200L).token(token).build();
                System.out.println("generated token : "+ token);
            }
            else {
                //500 : PASSWORD NOT MATCHED
                dto = UserDto.builder().state(500L).build();
            }
        }
        return dto;
    }

    @Override
    public UserDto signin(User user) {
        UserDto dto = UserDto.builder().build();
        try {
            String id = user.getId();
            String pw = user.getPw(); //asdfasdfasdf
            User foundUser = userRepo.findByid(id);
            if(foundUser == null){
                String encoderedPw = encoder.encode(pw);
                String role = user.getRole() == null ? "ROLE_USER" : user.getRole();
                User user1 = User.builder()
                        .id(id).pw(encoderedPw).role(role).useYn("y")
                        .build();
                em.persist(user1);
                return UserDto.builder().state(201L).build(); //회원가입 성공
            } else {
                return UserDto.builder().state(501L).build(); //기존 아이디 존재
            }
        } catch(Exception e){
            e.printStackTrace();
            return UserDto.builder().state(990L).build(); //에러
        }
    }

    @Override
    public Map<String, Object> changePw(Map<String, Object> data) {
        Map<String, Object> resultMap = new HashMap<>();
        String id = String.valueOf(data.get("id"));
        String oldPw = String.valueOf(data.get("asPw"));
        String newPw = String.valueOf(data.get("newPw"));
        User foundUser = userRepo.findByid(id);
        boolean result = encoder.matches(oldPw, foundUser.getPw());
        if(result){
            String encoderedPw = encoder.encode(newPw);
            foundUser.setPw(encoderedPw);
            resultMap.put("result", 200);
            resultMap.put("result_description", "비밀번호 변경완료");
        }else {
            resultMap.put("result", 903);
            resultMap.put("result_description", "기존 비밀번호가 다릅니다.");
        }
        return resultMap;
    }
}
