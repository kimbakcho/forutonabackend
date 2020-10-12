package com.wing.forutona.FTag.Repository;

import com.google.type.LatLng;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Domain.FBalltag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.FTag.Domain.QFBalltag.fBalltag;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FBallTagQueryRepository {

    @PersistenceContext
    EntityManager em;

    public List<FBalltag> findByBallInTags(List<FBall> fBalls) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(fBalltag).from(fBalltag).where(fBalltag.ballUuid.in(fBalls)).fetch();
    }

    public List<FBalltag> findByTextMatchTags(String matchText, LatLng mapCenter) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate<Integer> matchTextExpressions = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})", fBalltag.tagItem, "\'"+matchText+"\'");

        NumberTemplate st_distance = Expressions.numberTemplate(Double.class,
                "function('st_distance_sphere',{0},{1})", fBall.placePoint,
                GisGeometryUtil.createPoint(mapCenter.getLatitude(), mapCenter.getLongitude()));

        return queryFactory.select(fBalltag).from(fBalltag).join(fBalltag.ballUuid, fBall)
                .where(matchTextExpressions.eq(1))
                .orderBy(st_distance.asc())
                .limit(1000)
                .offset(0)
                .fetch();
    }


}
