package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.constants.ErrorCode;
import com.bandi.mhProject.dto.InfoDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.InfoRepository;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.WordService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WordServiceImpl implements WordService {
    private final InfoRepository infoRepo;
    private final UserRepository userRepo;
    @PersistenceContext
    EntityManager em;
    @Override
    public List<InfoDto> getInfoList() {
        List<InfoDto> infoList = new ArrayList<>();
        List<Info> originList = infoRepo.findAll();
        for(Info info: originList){
            InfoDto dto = InfoDto.builder().id(info.getId()).infokey(info.getInfokey()).info_kr(info.getInfo_kr()).link(info.getLink())
                    .description(info.getDescription())
                    .creator(info.getCreator()).updator(info.getUpdator())
                    .createDate(info.getCreateDate()).updateDate(info.getUpdateDate())
                    .build();
            infoList.add(dto);
        }
        return infoList;
    }

    @Override
    public Map<String, Object> addWord(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = String.valueOf(data.get("id"));
            User user = userRepo.findByid(userId);
            if(user != null){
                String writer = user.getId();
                String title = String.valueOf(data.get("title"));
                String contents = String.valueOf(data.get("contents"));
                Info info = Info.builder().infokey(title).description(contents).user(user)
                        .creator(writer).editor(writer)
                        .build();
                em.persist(info);
                result.put("result", 200);
                result.put("result_description", "작성완료");
            } else {
                result.put("result", 300);
                result.put("result_description", userId+"를 찾을 수 없습니다.");
            }
        }catch (Exception e){
            String errMsg = e.getMessage();
            result.put("result", 900);
            result.put("result_description", ErrorCode.ERROR_CODE_900.getMessage()+":"+errMsg);
            return result;
        }

        return result;
    }

    @Override
    public Map<String, Object> deleteWord(String id) {
        Map<String,Object> result = new HashMap();
        try{
            infoRepo.deleteById(id);
            result.put("result", 200);
            result.put("result_description", "delete complete");
        }catch(Exception e){
            String errMsg = e.getMessage();
            result.put("result", 900);
            result.put("result_description", errMsg);
            return result;
        }
        return result;
    }

    @Override
    public Map<String, Object> editWord(Map<String, Object> data) {
        Map<String,Object> result = new HashMap();
        try{
            String infoId = String.valueOf(data.get("wordId"));
            String title = String.valueOf(data.get("title"));
            String contents = String.valueOf(data.get("contents"));
            Info info = infoRepo.findById(infoId).orElse(null);
            if(info != null){
                info.setInfokey(title);
                info.setDescription(contents);
                result.put("result", 200);
                result.put("result_description", "complete");
            } else {
                result.put("result", 904);
                result.put("result_description", "업데이트된 내용 없음");
            }
        }catch(Exception e){
            String errMsg = e.getMessage();
            result.put("result", 900);
            result.put("result_description", ErrorCode.ERROR_CODE_900.getMessage()+":"+errMsg);
            return result;
        }
        return result;
    }

    @Override
    public InfoDto getWordById(String id) {
        InfoDto dto = null;
        try {
            Info rawInfo = infoRepo.findById(id).orElse(null);
            if(rawInfo != null){
                dto = InfoDto.builder().id(rawInfo.getId()).infokey(rawInfo.getInfokey())
                        .info_kr(rawInfo.getInfo_kr()).link(rawInfo.getLink())
                        .description(rawInfo.getDescription()).build();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public List<InfoDto> getWordList(String keyword) {
        List<InfoDto> infoList= new ArrayList<>();
        try{
            List<Info> rawList = new ArrayList<>();
            if(keyword.isBlank()){
                rawList = infoRepo.findAll();
            }else {
                rawList = infoRepo.findInfoByKeyword(keyword);
            }
            for(Info i : rawList){
                System.out.println(i.getInfokey()+":"+i.getDescription());
                InfoDto dto = InfoDto.builder()
                        .id(i.getId())
                        .infokey(i.getInfokey()).info_kr(i.getInfo_kr())
                        .description(i.getDescription()).link(i.getLink())
                        .creator(i.getCreator()).updator(i.getUpdator())
                        .createDate(i.getCreateDate()).updateDate(i.getUpdateDate())
                        .build();
                infoList.add(dto);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return infoList;
    }
}
