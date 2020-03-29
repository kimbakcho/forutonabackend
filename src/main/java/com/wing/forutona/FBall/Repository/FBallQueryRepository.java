package com.wing.forutona.FBall.Repository;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.GeometryExpression;
import com.querydsl.spatial.GeometryExpressions;
import com.querydsl.spatial.GeometryPath;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Dto.NearBallFindDistanceReqDto;
import com.wing.forutona.FBall.Dto.QFBallResDto;
import com.wing.forutona.FTag.Domain.QFBalltag;
import com.wing.forutona.FTag.Dto.QTagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingDto;
import org.geolatte.geom.codec.Wkt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;

@Repository
public class FBallQueryRepository {

    @PersistenceContext
    EntityManager em;

    /*
     ** 해당 Rect 내에 포함된 Ball의 갯수
     */
    public Long getFindBallCountInDistance(Geometry rect){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Long count = queryFactory.select(fBall.count()).from(fBall)
                .where(fBall.placePoint.within(rect)).fetchOne();
        return count;
    }


    /*
    CenterPoint와 rect 범위 안의 ball 들의 거리 반환
     */
    public List<FBallResDto> getFindBallInDistanceForQueryDsl(Geometry centerPoint,Geometry rect){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<FBallResDto> fetch = queryFactory.select(new QFBallResDto(fBall.placePoint,
                fBall.placePoint.distance(centerPoint)
        ))
                .from(fBall)
                .where(fBall.placePoint.within(rect))
                .fetch();
        return fetch;
    }










}
