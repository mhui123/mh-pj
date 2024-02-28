package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.Info;

import java.util.List;

public interface InfoCustomRepo {
    List<Info> findInfoByKeyword(String keyword);
    List<Info> findAllInfoList();
    List<Info> findMyInfoList(String userId);
}
