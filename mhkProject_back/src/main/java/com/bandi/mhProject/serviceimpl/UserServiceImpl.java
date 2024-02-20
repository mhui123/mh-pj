package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.UserService;
import com.bandi.mhProject.util.Encoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    @PersistenceContext
    EntityManager em;
    @Autowired
    private Encoder encoder;
    @Override
    public boolean login(User user) {
        String id = user.getId();
        String pw = user.getPw();
        User foundUser = userRepo.findByid(id);

        if(foundUser != null){
            return encoder.matchPwWithCompare(pw, foundUser.getPw());
        }
        return false;
    }

    @Override
    public boolean signin(User user) {
        try {
            String id = user.getId();
            String pw = user.getPw();
            String encoderedPw = encoder.encryptPw(pw);
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
