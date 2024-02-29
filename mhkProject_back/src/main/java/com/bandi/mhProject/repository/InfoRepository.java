package com.bandi.mhProject.repository;

import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.repository.custom.InfoCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InfoRepository extends JpaRepository<Info, String>, InfoCustomRepo, JpaSpecificationExecutor<Info>, QuerydslPredicateExecutor<Info> {
    Info findByCreator(String creator);
    List<Info> findInfoByKeyword(String keyword);
    List<Info> findAllInfoList();
    List<Info> findMyInfoList(Map<String, Object> data);
}
