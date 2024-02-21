package com.bandi.mhProject.entity;

import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.serviceimpl.UserServiceImpl;
import com.bandi.mhProject.util.Encoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class UserTest {
    @PersistenceContext
    EntityManager em;
    @Autowired UserRepository userRepo;
    @Autowired
    Encoder encoder;
    @Autowired
    UserServiceImpl impl;


    @Test
    public void signinTest(){
        String id = "test";
        String pw = "bandisnc01!";
        User user = new User(id, pw);

        id = user.getId();
        pw = user.getPw();
        String encoderedPw = encoder.encryptPw(pw);
        User user1 = new User(id, encoderedPw);
        em.persist(user1);
    }
}