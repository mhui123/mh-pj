package com.bandi.mhProject.serviceimpl;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            result.put("result", 901);
            result.put("result_description", errMsg);
            return result;
        }

        return result;
    }
}
