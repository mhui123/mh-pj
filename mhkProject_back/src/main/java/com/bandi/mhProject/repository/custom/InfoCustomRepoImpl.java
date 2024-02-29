package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.QInfo;
import com.bandi.mhProject.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
    public List<Info> findMyInfoList(String userId) {
        return factory.selectFrom(QInfo.info)
                .join(QInfo.info.user, QUser.user)
                .where(QUser.user.id.eq(userId))
                .fetch();
    }
}
