package com.bandi.mhProject.repository;

import com.bandi.mhProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByid(String userId);
}
