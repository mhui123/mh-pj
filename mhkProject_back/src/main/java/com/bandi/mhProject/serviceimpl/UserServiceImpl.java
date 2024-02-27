package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.component.JwtTokenProvider;
import com.bandi.mhProject.config.auth.PrincipalDetail;
import com.bandi.mhProject.constants.ErrorCode;
import com.bandi.mhProject.dto.JwtToken;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.UserService;
import com.bandi.mhProject.util.Encoder;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
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

import java.util.ArrayList;
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
                    result.put("result_description", "비밀번호를 확인해주세요");
                }
            }
        } catch(Exception e){
            result.put("result", 900);
            result.put("result_description", "에러발생. msg:" + e.getMessage());
        }

        return result;
    }
    // signin returnType Map<String, Object>로 통일해야함.
    @Override
    public Map<String ,Object> signin(User user) {
        Map<String ,Object> result = new HashMap<>();
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
                result.put("result", 200);
                result.put("result_description", "회원가입 완료");
            } else {
                String errorMsg = ErrorCode.ERROR_CODE_903.getMessage();
                result.put("result", 903);
                result.put("result_description", errorMsg);
            }
        } catch(Exception e){
            e.printStackTrace();
            String errorMsg = ErrorCode.ERROR_CODE_900.getMessage() + " : " + e.getMessage();
            result.put("result", 900);
            result.put("result_description", errorMsg);
        }
        return result;
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
            resultMap.put("result", 902);
            resultMap.put("result_description", ErrorCode.ERROR_CODE_902.getMessage());
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
        List<String> ids = null;
        List<String> failedId = new ArrayList<>();
//        JsonObject obj = new JSONObject(jsonString)
        try{
            if(data.containsKey("users") && data.get("users") instanceof ArrayList<?>){
                ids = (List<String>) data.get("users");
                for(String id : ids){
                    User user = userRepo.findByid(id);
                    if(user != null){
                        String toChange = user.getRole().equals("ROLE_USER") ? "ROLE_ADMIN" : "ROLE_USER";
                        user.setRole(toChange);
                    } else {
                        failedId.add(id);
                    }
                }
                if(!failedId.isEmpty()){
                    String errorMsg = ErrorCode.ERROR_CODE_901.getMessage() + failedId.toString();
                    result.put("result", 901);
                    result.put("result_description", errorMsg);
                } else {
                    result.put("result", 200);
                    result.put("result_description", "권한변경 성공");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> changeUseYn(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        List<String> ids = null;
        List<String> failedId = new ArrayList<>();
//        JsonObject obj = new JSONObject(jsonString)
        try{
            if(data.containsKey("users") && data.get("users") instanceof ArrayList<?>){
                ids = (List<String>) data.get("users");
                for(String id : ids){
                    User user = userRepo.findByid(id);
                    if(user != null){
                        String toChange = user.getUseYn().equals("y") ? "n" : "y";
                        user.setUseYn(toChange);
                    } else {
                        failedId.add(id);
                    }
                }
                if(!failedId.isEmpty()){
                    String errorMsg = ErrorCode.ERROR_CODE_901.getMessage() + failedId.toString();
                    result.put("result", 901);
                    result.put("result_description", errorMsg);
                } else {
                    result.put("result", 200);
                    result.put("result_description", "사용여부 변경 완료");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> initializePw(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        List<String> ids = new ArrayList<>();
        List<String> failedId = new ArrayList<>();

        try{
            if(data.containsKey("ids") && data.get("ids") instanceof ArrayList<?>){
                ids = (List<String>) data.get("ids");
                for(String id : ids){
                    User user = userRepo.findByid(id);
                    if(user != null){
                        String initPw = "bandisnc01!";
                        String encoderedPw = encoder.encode(initPw);
                        user.setPw(encoderedPw);
                    } else {
                        failedId.add(id);
                    }
                }
                if(!failedId.isEmpty()){
                    String errorMsg = ErrorCode.ERROR_CODE_904.getMessage() + failedId.toString();
                    result.put("result", 904);
                    result.put("result_description", errorMsg);
                } else {
                    result.put("result", 200);
                    result.put("result_description", "비밀번호 초기화 성공");
                }
            }
        }catch(Exception e){
            result.put("result", 900);
            result.put("result_description", "에러발생. msg:" + e.getMessage());
        }

        return result;
    }
}
