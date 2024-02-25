package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.Info;
import com.bandi.mhProject.entity.QInfo;
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
                .where(QInfo.info.infokey.like("%"+keyword+"%")
                        .or(QInfo.info.info_kr.like("%"+keyword+"%"))
                        .or(QInfo.info.description.like("%"+keyword+"%"))
        ).fetch();
    }
}
