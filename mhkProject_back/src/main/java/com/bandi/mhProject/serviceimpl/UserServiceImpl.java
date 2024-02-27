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
    public Map<String ,Object> login(User user) {
        Map<String ,Object> result = new HashMap<>();
        try{
            String id = user.getId();
            String pw = user.getPw();
            User foundUser = userRepo.findByid(id);
            JwtToken token = null;

            UserDto dto = UserDto.builder().build();
            if(foundUser != null){
//            return encoder.matchPwWithCompare(pw, foundUser.getPw());
                boolean chk = encoder.matches(pw, foundUser.getPw());
                if(chk){
                    id = foundUser.getId();
                    String role = foundUser.getRole();
                    String useYn = foundUser.getUseYn();
                    if(useYn.equals("y")){
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, pw);
                        Authentication authentication = authBuilder.getObject().authenticate(authToken); //authenticationManager.authenticate(authToken);
                        token = jwtTokenProvider.generateToken(authentication);
                        dto = UserDto.builder().id(id).role(role).authorized(true).token(token).build();
                        result.put("result", 200);
                        result.put("result_description", "로그인성공");
                        result.put("userInfo", dto);
                    } else {
                        result.put("result", 904);
                        result.put("result_description", "사용중지상태인 계정입니다.");
                    }
                }
                else {
                    //500 : PASSWORD NOT MATCHED
                    result.put("result", 905);
                    result.put("result_description", "PASSWORD NOT MATCHED");
                }
            }
        } catch(Exception e){
            result.put("result", 900);
            result.put("result_description", "에러발생. msg:" + e.getMessage());
        }

        return result;
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

    @Override
    public List<UserDto> getUserList() {
        return userRepo.findUserList();
    }

    @Override
    public Map<String, Object> changeRole(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try{
            String id = String.valueOf(data.get("id"));
            String role = String.valueOf(data.get("role"));
            String toChange = role.equals("ROLE_USER") ? "ROLE_ADMIN" : "ROLE_USER";
            String toChangeTxt = toChange.equals("ROLE_USER") ? "유저" : "관리자";
            User user = userRepo.findByid(id);
            if(user != null){
                user.setRole(toChange);
                result.put("result", 200);
                result.put("result_description", toChangeTxt + "로 권한변경되었습니다.");
            } else {
                result.put("result", 902);
                result.put("result_description", "유저를 찾을 수 없습니다.");
            }

        }catch(Exception e){
            result.put("result", 900);
            result.put("result_description", "에러발생. msg:" + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> changeUseYn(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try{
            String id = String.valueOf(data.get("id"));
            String useYn = String.valueOf(data.get("useYn")).toLowerCase();
            String toChange = useYn.equals("y") ? "n" : "y";
            String toChangeTxt = toChange.equals("y") ? "사용" : "중지";
            User user = userRepo.findByid(id);
            if(user != null){
                user.setUseYn(toChange);
                result.put("result", 200);
                result.put("result_description", "유저사용 여부를 "+toChangeTxt+ "로 변경 하였습니다.");
            } else {
                result.put("result", 902);
                result.put("result_description", "유저를 찾을 수 없습니다.");
            }
        } catch(Exception e){
            result.put("result", 900);
            result.put("result_description", "에러발생. msg:" + e.getMessage());
        }
        return result;
    }
}
