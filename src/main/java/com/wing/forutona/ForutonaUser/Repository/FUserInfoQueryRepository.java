package com.wing.forutona.ForutonaUser.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Dto.QFUserInfoResDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FUserInfoQueryRepository {

    @PersistenceContext
    EntityManager em;


    public FUserInfoResDto getBasicUserInfo(FUserReqDto fUserReqDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .select(new QFUserInfoResDto(fUserInfo))
                .from(fUserInfo)
                .where(fUserInfo.uid.eq(fUserReqDto.getUid()))
                .fetchOne();
    }
}
