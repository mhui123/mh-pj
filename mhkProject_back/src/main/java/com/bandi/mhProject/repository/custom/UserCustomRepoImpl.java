package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.dto.UserDto;
import com.bandi.mhProject.entity.QInfo;
import com.bandi.mhProject.entity.QUser;
import com.bandi.mhProject.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserCustomRepoImpl implements UserCustomRepo{
    private final EntityManager em;
    private final JPAQueryFactory factory;

    @Override
    public List<UserDto> findUserList() {
        //추후 페이징용 구문 추가
        List<User> userList = factory.selectFrom(QUser.user).fetch();
        List<UserDto> dtoList = new ArrayList<>();
        for(User u : userList){
            UserDto dto = UserDto.builder()
                    .id(u.getId()).role(u.getRole()).useYn(u.getUseYn())
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
    }
}
