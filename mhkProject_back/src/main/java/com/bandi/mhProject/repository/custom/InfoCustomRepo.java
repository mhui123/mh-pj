package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.Info;

import java.util.List;
import java.util.Map;

public interface InfoCustomRepo {
    List<Info> findInfoByKeyword(String keyword);
    List<Info> findAllInfoList();
    List<Info> findMainInfoList(long pageIdx);
    List<Info> findMyInfoList(Map<String, Object> data);
}
