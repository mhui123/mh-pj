package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.ManageKey;

import java.util.List;

public interface ManageKeyCustomRepo {
    ManageKey findValidKeyByUserId(String userId);
}
