package com.bandi.mhProject.repository.custom;

import com.bandi.mhProject.entity.ManageKey;
import com.bandi.mhProject.entity.QInfo;
import com.bandi.mhProject.entity.QManageKey;
import com.bandi.mhProject.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ManageKeyCustomRepoImpl implements ManageKeyCustomRepo{
    private final EntityManager em;
    private final JPAQueryFactory factory;
    @Override
    public List<ManageKey> findValidKeyByUserId(String userId) {
        return factory.selectFrom(QManageKey.manageKey)
                .join(QManageKey.manageKey.user, QUser.user)
                .where(QManageKey.manageKey.useYn.eq("y")
                        .and(QUser.user.id.eq(userId))
                )
                .fetch();
    }
}
