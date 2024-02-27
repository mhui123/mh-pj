package com.bandi.mhProject.repository;

import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.custom.InfoCustomRepo;
import com.bandi.mhProject.repository.custom.UserCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String>, UserCustomRepo, JpaSpecificationExecutor<Info>, QuerydslPredicateExecutor<Info> {
    User findByid(String userId);
    List<UserDto> findUserList();
}
