package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.component.JwtTokenProvider;
import com.bandi.mhProject.constants.SystemConfigs;
import com.bandi.mhProject.dto.JwtToken;
import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.ManageKey;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.ManageKeyRepository;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.UserService;
import com.bandi.mhProject.util.Commons;
import com.bandi.mhProject.util.KeyGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService, SystemConfigs {
    private final UserRepository userRepo;
    @Autowired ManageKeyRepository keyRepo;
    private final AuthenticationManagerBuilder authBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired private AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepo, AuthenticationManagerBuilder authBuilder, JwtTokenProvider jwtTokenProvider
            , PasswordEncoder encoder) {
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
                    if("y".equals(useYn)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, pw);
                        Authentication authentication = authBuilder.getObject().authenticate(authToken); //authenticationManager.authenticate(authToken);
                        token = jwtTokenProvider.generateToken(authentication);
                        dto = UserDto.builder().id(id).role(role).authorized(true).token(token).build();
                        Commons.putMessage(result, 200, CODE_203);
                        result.put("userInfo", dto);
                    } else {
                        Commons.putMessage(result, 906, CODE_906);
                    }
                }
                else {
                    Commons.putMessage(result, 907, CODE_907);
                }
            } else {
                Commons.putMessage(result, 901, CODE_901);
            }
        } catch(Exception e){
            Commons.putMessage(result, 900, CODE_900);
        }

        return result;
    }
    // signin returnType Map<String, Object>로 통일해야함.
    @Override
    public Map<String ,Object> signin(Map<String, Object> data) {
        Map<String ,Object> result = new HashMap<>();
        try {
            String id = String.valueOf(data.get("id"));
            String pw = String.valueOf(data.get("pw"));
            String name = String.valueOf(data.get("name"));
            User foundUser = userRepo.findByid(id);
            if(foundUser == null){
                String encoderedPw = encoder.encode(pw);
                String role = "ROLE_USER";
                User user = User.builder()
                        .id(id).pw(encoderedPw).role(role).useYn("y")
                        .build();
                em.persist(user);
                Commons.putMessage(result, 200, CODE_204);
            } else {
                Commons.putMessage(result, 903, CODE_903);
            }
        } catch(Exception e){
            String errorMsg = CODE_900 + " : " + e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
        }
        return result;
    }

    @Override
    public Map<String, Object> changePw(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String id = String.valueOf(data.get("id"));
        String oldPw = String.valueOf(data.get("asPw"));
        String newPw = String.valueOf(data.get("newPw"));
        String mode = String.valueOf(data.get("mode"));
        User foundUser = userRepo.findByid(id);
        ManageKey foundKey = keyRepo.findValidKeyByUserId(id);
        if("fetch".equals(mode)){
            String encoderedPw = encoder.encode(newPw);
            foundUser.setPw(encoderedPw);
            if(foundKey != null){
                keyRepo.delete(foundKey);
                em.flush();
                em.clear();
            }
            Commons.putMessage(result, 202, CODE_202);
        }else {
            boolean isMatch = encoder.matches(oldPw, foundUser.getPw());
            if(isMatch){
                String encoderedPw = encoder.encode(newPw);
                foundUser.setPw(encoderedPw);
                Commons.putMessage(result, 200, "비밀번호 변경완료");
            }else {
                Commons.putMessage(result, 902, CODE_902);
            }
        }
        return result;
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
                        String toChange = "ROLE_USER".equals(user.getRole()) ? "ROLE_ADMIN" : "ROLE_USER";
                        user.setRole(toChange);
                    } else {
                        failedId.add(id);
                    }
                }
                if(!failedId.isEmpty()){
                    String errorMsg = CODE_901 + failedId.toString();
                    Commons.putMessage(result, 901, errorMsg);
                } else {
                    Commons.putMessage(result, 200, CODE_205);
                }
            }
        } catch(Exception e){
            String errorMsg = CODE_900+" : "+e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
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
                        String toChange = "y".equals(user.getUseYn()) ? "n" : "y";
                        user.setUseYn(toChange);
                    } else {
                        failedId.add(id);
                    }
                }
                if(!failedId.isEmpty()){
                    String errorMsg = CODE_901 + failedId.toString();
                    Commons.putMessage(result, 901, errorMsg);
                } else {
                    Commons.putMessage(result, 200, CODE_206);
                }
            }
        } catch(Exception e){
            String errorMsg = CODE_900+" : "+e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
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
                    String errorMsg = CODE_904 + failedId.toString();
                    Commons.putMessage(result, 904, errorMsg);
                } else {
                    Commons.putMessage(result, 200, CODE_207);
                }
            }
        }catch(Exception e){
            String errorMsg = CODE_900 + " : " + e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
        }

        return result;
    }

    @Override
    public Map<String, Object> getUserListByKeyword(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String keyword = String.valueOf(data.get("keyword"));
        List<UserDto> dtoList = null;
        try{
            dtoList = userRepo.findUserListByKeyword(keyword);
            String msg = "검색결과: "+ dtoList.size() + "건";
            Commons.putMessage(result, 200, msg);
        } catch(Exception e){
            String errorMsg = CODE_900 + " : " + e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
        }
        result.put("list", dtoList);
        return result;
    }

    @Override
    public Map<String, Object> generateAuthKey(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String userId = String.valueOf(data.get("id"));
        try{
            if(!"null".equals(userId)){
                ManageKey foundKey = keyRepo.findValidKeyByUserId(userId);
                String isRetry = String.valueOf(data.get("retry"));
                if("y".equals(isRetry) && foundKey != null){
                    //재인증요청. 기존에 존재하는 키 제거
                    keyRepo.delete(foundKey);
                    em.flush();
                    em.clear();
                    foundKey = null;
                }
                if(foundKey == null){
                    User user = userRepo.findByid(userId);
                    if(user != null){
                        String authKey = KeyGenerator.generateKey(); //키생성
                        ManageKey manageKey = ManageKey.builder().authKey(authKey).user(user).build();
                        em.persist(manageKey);
                        result.put("authKey", authKey);
                        Commons.putMessage(result,200, CODE_200);
                    } else {
                        Commons.putMessage(result,901, CODE_901);
                    }
                } else {
                    if(isInTimeKey(foundKey)){ //유효시간 경과여부 체크
                        Commons.putMessage(result,908, CODE_908); //아직 유효키 존재
                    } else { //유효시간 경과. 기존키 제거 후 메서드 재실행
                        keyRepo.delete(foundKey);
                        em.flush();
                        em.clear();
                        result = generateAuthKey(data);
                    }
                }
            } else {
                Commons.setMessage(result, CODE_909); //파라미터 불완전
            }
        }catch(Exception e){
            Commons.setMessage(result, CODE_900 +" : " +e.getMessage());
            return result;
        }
        return result;
    }

    private boolean isInTimeKey(ManageKey key){
        LocalDateTime now = LocalDateTime.now();
        return KeyGenerator.isInTimeAuthenticated(key.getCreateDate(), now);
    }

    @Override
    public Map<String, Object> validateAuthKey(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String userId = String.valueOf(data.get("id"));
        try{
            if(!"null".equals(userId) && Strings.isNotEmpty(userId)){
                ManageKey storeKeyEntity = keyRepo.findValidKeyByUserId(userId);
                if(storeKeyEntity != null){
                    if(isInTimeKey(storeKeyEntity)){
                        String storeKey = storeKeyEntity.getAuthKey();
                        String inputKey = String.valueOf(data.get("inputKey"));
                        boolean isValid = KeyGenerator.validateKey(inputKey, storeKey);
                        if(isValid){
                            Commons.putMessage(result, 201, CODE_201); //인증성공
                        } else {
                            Commons.putMessage(result, 910, CODE_910); //인증키 불일치
                        }
                    } else if(!isInTimeKey(storeKeyEntity)){
                        Commons.putMessage(result, 911, CODE_911); //인증키 유효시간경과
                    }
                } else {
                    Commons.putMessage(result, 912, CODE_912); //인증과정 오류
                }
            } else {
                Commons.setMessage(result, CODE_909); //파라미터 불완전.
            }
        }catch(Exception e){
            Commons.setMessage(result, CODE_900 +" : " +e.getMessage());
            return result;
        }
        return result;
    }
}
