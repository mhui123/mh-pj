package com.bandi.mhProject.repository;

import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.ManageKey;
import com.bandi.mhProject.repository.custom.ManageKeyCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManageKeyRepository extends JpaRepository<ManageKey, String>, ManageKeyCustomRepo {
    ManageKey findAuthKeyByUserId(String userId);
    List<ManageKey>  findValidKeyByUserId(String userId);
}
