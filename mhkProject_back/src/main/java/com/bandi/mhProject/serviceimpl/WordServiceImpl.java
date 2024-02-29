package com.bandi.mhProject.serviceimpl;

import com.bandi.mhProject.constants.ErrorCode;
import com.bandi.mhProject.dto.InfoDto;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.User;
import com.bandi.mhProject.repository.InfoRepository;
import com.bandi.mhProject.repository.UserRepository;
import com.bandi.mhProject.service.WordService;
import com.bandi.mhProject.util.Commons;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
            InfoDto dto = setInfoDto(info);
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
                String link = data.containsKey("link") ? String.valueOf(data.get("link")) : "none";
                Info info = Info.builder().infokey(title).description(contents).user(user)
                        .creator(writer).editor(writer).link(link)
                        .build();
                em.persist(info);
                Commons.putMessage(result, 200, "작성완료");
            } else {
                Commons.putMessage(result, 903, ErrorCode.ERROR_CODE_901.getMessage()+":"+userId);
            }
        }catch (Exception e){
            String errMsg = ErrorCode.ERROR_CODE_900.getMessage()+" : "+e.getMessage();
            Commons.putMessage(result, 900, errMsg);
            return result;
        }

        return result;
    }

    @Override
    public Map<String, Object> deleteWord(String id) {
        Map<String,Object> result = new HashMap();
        try{
            infoRepo.deleteById(id);
            Commons.putMessage(result, 200, "삭제완료");
        }catch(Exception e){
            String errMsg = ErrorCode.ERROR_CODE_900.getMessage()+" : "+e.getMessage();
            Commons.putMessage(result, 900, errMsg);
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
            String link = data.containsKey("link") ? String.valueOf(data.get("link")) : null;
            Info info = infoRepo.findById(infoId).orElse(null);
            if(info != null){
                info.setInfokey(title);
                info.setDescription(contents);
                info.setLink(link);
                Commons.putMessage(result, 200, "작성완료");
            } else {
                String errorMsg = ErrorCode.ERROR_CODE_905.getMessage()+":"+infoId;
                Commons.putMessage(result, 905, errorMsg);
            }
        }catch(Exception e){
            String errorMsg = ErrorCode.ERROR_CODE_900.getMessage()+" : "+e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
            return result;
        }
        return result;
    }

    @Override
    public InfoDto getWordById(Map<String, Object> data) {
        String id = String.valueOf(data.get("id"));
        InfoDto dto = null;
        try {
            Info rawInfo = infoRepo.findById(id).orElse(null);
            if(rawInfo != null){
                dto = setInfoDto(rawInfo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public Map<String, Object> getWordListByKeyword(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        List<InfoDto> infoList= new ArrayList<>();
        try{
            String keyword = String.valueOf(data.get("keyword"));
            List<Info> rawList = new ArrayList<>();
            if(keyword.isBlank()){
                rawList = infoRepo.findAll();
            }else {
                rawList = infoRepo.findInfoByKeyword(keyword);
            }
            for(Info i : rawList){
                infoList.add(setInfoDto(i));
            }
            String msg = "검색결과 : "+infoList.size()+"건";
            Commons.putMessage(result, 200, msg);
        }
        catch(Exception e){
            String errorMsg = ErrorCode.ERROR_CODE_900.getMessage()+" : "+e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
        }
        result.put("list", infoList);
        return result;
    }

    @Override
    public List<InfoDto> getAllWordList() {
        List<Info> rawData = infoRepo.findAllInfoList();
        List<InfoDto> list = new ArrayList<>();
        for(Info info : rawData){
            InfoDto dto = setInfoDto(info);
            list.add(dto);
        }
        return list;
    }

    @Override
    public Map<String, Object> findMyInfoList(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String userId = String.valueOf(data.get("id"));
        String keyword = String.valueOf(data.get("keyword"));
        List<InfoDto> list = new ArrayList<>();
        if(Strings.isNotEmpty(userId)){
            List<Info> raw = infoRepo.findMyInfoList(data);
            for(Info i : raw){
                InfoDto dto = setInfoDto(i);
                list.add(dto);
            }
            result.put("list", list);
            String msg = "검색결과 : "+list.size()+"건";
            Commons.putMessage(result, 200, msg);
        }

        return result;
    }

    @Override
    public Map<String, Object> delWords(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try{
            JSONParser parser = new JSONParser();
            ObjectMapper objMapper = new ObjectMapper();
            String jsonStr = objMapper.writeValueAsString(data);
            JSONObject jObj = (JSONObject) parser.parse(jsonStr);
            List<String> ids = (List<String>) jObj.get("ids");
            for(String id : ids){
                infoRepo.deleteById(id);
            }
            Commons.putMessage(result, 200, "삭제완료");
        } catch(Exception e){
            String errorMsg = ErrorCode.ERROR_CODE_900.getMessage()+" : "+e.getMessage();
            Commons.putMessage(result, 900, errorMsg);
        }
        return result;
    }

    private InfoDto setInfoDto(Info info){
        return InfoDto.builder().id(info.getId()).infokey(info.getInfokey()).info_kr(info.getInfo_kr()).link(info.getLink())
                .description(info.getDescription())
                .creator(info.getCreator()).updator(info.getUpdator())
                .createDate(info.getCreateDate()).updateDate(info.getUpdateDate())
                .build();
    }
}
