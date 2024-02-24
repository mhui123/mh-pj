package com.bandi.mhProject.repository;

import com.bandi.mhProject.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository extends JpaRepository<Info, String> {
    Info findByCreator(String creator);
}
