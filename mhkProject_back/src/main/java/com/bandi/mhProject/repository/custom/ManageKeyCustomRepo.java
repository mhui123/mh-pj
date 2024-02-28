package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.ManageKey;

import java.util.List;

public interface ManageKeyCustomRepo {
    List<ManageKey> findValidKeyByUserId(String userId);
}
