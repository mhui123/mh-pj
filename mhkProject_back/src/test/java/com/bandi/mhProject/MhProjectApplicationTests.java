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
}
