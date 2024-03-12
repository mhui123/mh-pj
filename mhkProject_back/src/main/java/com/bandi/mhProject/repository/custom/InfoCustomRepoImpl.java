package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.constants.SystemConfigs;
import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.QInfo;
import com.bandi.mhProject.entity.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class InfoCustomRepoImpl implements InfoCustomRepo{
    private final EntityManager em;
    private final JPAQueryFactory factory;
    @Override
    public List<Info> findInfoByKeyword(String keyword) {
        return factory.selectFrom(QInfo.info)
                .join(QInfo.info.user, QUser.user)
                .where(QInfo.info.infokey.like("%"+keyword+"%")
                        .or(QInfo.info.info_kr.like("%"+keyword+"%"))
                        .or(QInfo.info.description.like("%"+keyword+"%")
                        .or(QUser.user.id.like("%"+keyword+"%"))
                        )
        ).fetch();
    }

    @Override
    public List<Info> findAllInfoList() {
        //추후 페이징 기능 추가
        return factory.selectFrom(QInfo.info).fetch();
    }

    @Override
    public List<Info> findMainInfoList(long pageIdx) {
        long offset = SystemConfigs.PAGE_SIZE * pageIdx;
        return factory.selectFrom(QInfo.info).offset(offset).limit(SystemConfigs.PAGE_SIZE).fetch();
    }

    @Override
    public List<Info> findMyInfoList(Map<String, Object> data) {
        String userId = String.valueOf(data.get("id"));
        String keyword = String.valueOf(data.get("keyword"));
        long pageIdx = Long.parseLong(String.valueOf(data.get("pageIdx")));
        long offset = SystemConfigs.PAGE_SIZE * pageIdx;
        List<Info> list = null;
        JPAQuery<Info> q = factory.selectFrom(QInfo.info)
                .join(QInfo.info.user, QUser.user);
        if(keyword.isBlank() || "null".equals(keyword)){
           q.where(QUser.user.id.eq(userId)).offset(offset).limit(SystemConfigs.PAGE_SIZE);
        } else {
            q.where(QUser.user.id.eq(userId)
                    .and(   QInfo.info.infokey.like("%"+keyword+"%")
                            .or(QInfo.info.description.like("%"+keyword+"%"))
                    )
            );
        }
        list = q.fetch();
        return list;
    }

    @Override
    public long getTotalCnt() {
        return factory.selectFrom(QInfo.info).fetch().size();
    }
}
