package com.wing.forutona.App.FTag.Repository;

import com.google.type.LatLng;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FTag.Domain.FBalltag;
import com.wing.forutona.App.FTag.Dto.FBallTagResDto;
import com.wing.forutona.App.FTag.Dto.TextMatchTagBallReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.wing.forutona.App.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.App.FTag.Domain.QFBalltag.fBalltag;

@Repository
public class FBallTagQueryRepository   {

    @PersistenceContext(unitName = "forutonaApp")
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

    public Page<FBallTagResDto> findByTagItem(TextMatchTagBallReqDto reqDto, Pageable pageable) throws ParseException {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        LatLng mapCenter = LatLng.newBuilder()
                .setLatitude(reqDto.getMapCenterLatitude())
                .setLongitude(reqDto.getMapCenterLongitude())
                .build();


        JPAQuery<FBalltag> tempQuery = queryFactory.select(fBalltag)
                .from(fBalltag).join(fBalltag.ballUuid, fBall)
                .where(fBalltag.tagItem.eq(reqDto.getSearchText()));

        JPAQuery<FBalltag> tempQuery1 = applySort(tempQuery, pageable, mapCenter);

        JPAQuery<FBalltag> resultQuery = tempQuery1.limit(pageable.getPageSize()).offset(pageable.getOffset());

        Page<FBalltag> page = PageableExecutionUtils.getPage(resultQuery.fetch(), pageable, tempQuery::fetchCount);

        return page.map(x-> new FBallTagResDto(x));
    }

    public JPAQuery<FBalltag> applySort(JPAQuery<FBalltag> query,Pageable pageable,LatLng mapCenter) throws ParseException {
        PathBuilder<FBall> entityPath = new PathBuilder<FBall>(FBall.class, "fBall");
        for (Sort.Order order : pageable.getSort()) {
            if(order.getProperty().equals("distance")){
                NumberTemplate st_distance = Expressions.numberTemplate(Double.class,
                        "function('st_distance_sphere',{0},{1})", fBall.placePoint,
                        GisGeometryUtil.createPoint(mapCenter.getLatitude(), mapCenter.getLongitude()));
                query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name()), st_distance));
            } else {
                PathBuilder<Object> path = entityPath.get(order.getProperty());
                query.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name()), path));
            }
        }
        return query;
    }

}
