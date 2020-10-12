package com.wing.forutona.ForutonaUser.Repository;


import com.google.type.LatLng;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import com.wing.forutona.ForutonaUser.Dto.QFUserInfoResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import java.util.List;

import static com.wing.forutona.FTag.Domain.QFBalltag.fBalltag;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FUserInfoQueryRepository {

    @PersistenceContext
    EntityManager em;

    public List<FUserInfoResDto> getFindNearUserFromGeoLocation(LatLng latLng,double distance) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate st_distance = Expressions.numberTemplate(Double.class,
                "function('st_distance_sphere',{0},{1})", fUserInfo.placePoint,
                GisGeometryUtil.createPoint(latLng.getLatitude(), latLng.getLongitude()));

        List<FUserInfoResDto> result = queryFactory.select(new QFUserInfoResDto(fUserInfo))
                .from(fUserInfo)
                .where(st_distance.loe(distance))
                .fetch();

        return result;
    }

    public Page<FUserInfoSimpleResDto> findByUserNickNameWithFullTextMatchIndex(String searchNickName, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate<Integer> matchTextExpressions = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})", fUserInfo.nickName, "\'"+searchNickName+"\'");

        QueryResults<FUserInfo> results = queryFactory.select(fUserInfo)
                .from(fUserInfo)
                .where(matchTextExpressions.eq(1))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(fUserInfo.playerPower.desc())
                .fetchResults();

        Page<FUserInfo> fUserInfos = new PageImpl<FUserInfo>(results.getResults(), pageable, results.getTotal());

        Page<FUserInfoSimpleResDto> fUserInfoSimpleResults = fUserInfos.map(x -> new FUserInfoSimpleResDto(x));

        return fUserInfoSimpleResults;
    }
}
