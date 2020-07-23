package com.wing.forutona.ForutonaUser.Repository;

import com.google.type.LatLng;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Dto.QFUserInfoResDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
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

    public List<FUserInfoResDto> getFindNearUserFromGeoLocation(LatLng latLng,double distance) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate st_distance = Expressions.numberTemplate(Double.class,
                "function('st_distance_sphere',{0},{1})", fUserInfo.placePoint,
                GisGeometryUtil.createCenterPoint(latLng.getLatitude(), latLng.getLongitude()));

        List<FUserInfoResDto> result = queryFactory.select(new QFUserInfoResDto(fUserInfo))
                .from(fUserInfo)
                .where(st_distance.loe(distance))
                .fetch();

        return result;
    }
}
