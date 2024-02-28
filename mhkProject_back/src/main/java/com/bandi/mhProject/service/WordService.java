package com.bandi.mhProject.service;

import com.bandi.mhProject.dto.InfoDto;
import com.bandi.mhProject.entity.Info;

import java.util.List;
import java.util.Map;

public interface WordService {
    public List<InfoDto> getInfoList();
    Map<String, Object> addWord(Map<String, Object> data);
    Map<String, Object> deleteWord(String id);
    Map<String, Object> editWord(Map<String, Object> data);

    InfoDto getWordById(String id);
    List<InfoDto> getWordList(String keyword);
    List<InfoDto> getAllWordList();
    List<InfoDto> findMyInfoList(Map<String, Object> data);

    Map<String, Object> delWords(Map<String, Object> data);
}
