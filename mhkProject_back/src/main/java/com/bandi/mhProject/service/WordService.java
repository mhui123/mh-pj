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

    InfoDto getWordById(Map<String, Object> data);
    Map<String, Object> getWordListByKeyword(Map<String, Object> data);
    List<InfoDto> getAllWordList();
    Map<String, Object> findMyInfoList(Map<String, Object> data);

    Map<String, Object> delWords(Map<String, Object> data);

    Map<String, Object> getInfoListWithPage(long pageIdx);
}
