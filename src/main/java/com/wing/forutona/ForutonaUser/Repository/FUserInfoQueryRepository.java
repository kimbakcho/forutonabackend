package com.wing.forutona.ForutonaUser.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FUserInfoQueryRepository {

    @PersistenceContext
    EntityManager em;


    public FUserInfoDto getBasicUserInfo(FUserReqDto fUserReqDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(Projections.bean(FUserInfoDto.class,
                        fUserInfo.uid, fUserInfo.nickName, fUserInfo.profilePicktureUrl
                        , fUserInfo.gender, fUserInfo.ageDate, fUserInfo.email, fUserInfo.snsService,
                        fUserInfo.userLevel, fUserInfo.expPoint, fUserInfo.followCount))
                .from(fUserInfo)
                .where(fUserInfo.uid.eq(fUserReqDto.getUid()))
                .fetchOne();
    }
}
