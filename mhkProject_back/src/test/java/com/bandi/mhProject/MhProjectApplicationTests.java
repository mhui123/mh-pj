package com.bandi.mhProject;

import com.bandi.mhProject.entity.ManageKey;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.ManageKeyRepository;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.util.Commons;
import com.bandi.mhProject.util.KeyGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
class MhProjectApplicationTests {
	@Autowired
	UserRepository userRepo;
	@Autowired
	ManageKeyRepository keyRepo;
	@PersistenceContext
	EntityManager em;
	@Test
	void contextLoads() {
	}

	@Test
	void test(){
		String userId = "mhui123@naver.com";
		Map<String, Object> result = new HashMap<>();
		ManageKey foundKey = keyRepo.findValidKeyByUserId(userId);
		if(foundKey == null){
			User user = userRepo.findByid(userId);
			if(user != null){
				String authKey = KeyGenerator.generateKey();
				ManageKey manageKey = ManageKey.builder().authKey(authKey).user(user).build();
				em.persist(manageKey);
				result.put("authKey", authKey);
			}
		} else {
			LocalDateTime now = LocalDateTime.now();
			//키는 존재하지만 유효기간이 지났는지 여부 체크
			boolean isIntime = KeyGenerator.isInTimeAuthenticated(foundKey.getCreateDate(), now);
			if(isIntime){

			} else {
				//유효시간 경과. 기존키 제거 후새로발급
				keyRepo.delete(foundKey); //기존키 제거
				em.flush();
				em.clear();
			}
		}
	}

	@Test
	void authenticateTest(){
		String userId = "mhui123@naver.com";
			if(!userId.equals("null") && Strings.isNotEmpty(userId)){
				ManageKey storeKeyEntity = keyRepo.findValidKeyByUserId(userId);
				String storeKey = storeKeyEntity.getAuthKey();
				String inputKey = "d31b3c123123";
				boolean isValid = KeyGenerator.validateKey(inputKey, storeKey);
				if(isValid){
					//인증성공.
					System.out.println("success authenticate");
				} else {
					//인증실패
					System.out.println("fail authenticate");
				}
			}
	}

}
