package com.bandi.mhProject.service;

import com.bandi.mhProject.dto.InfoDto;
import com.bandi.mhProject.entity.Info;

import java.util.List;
import java.util.Map;

public interface WordService {
    public List<InfoDto> getInfoList();
    Map<String, Object> addWord(Map<String, Object> data);
    Map<String, Object> deleteWord(String id);
}
