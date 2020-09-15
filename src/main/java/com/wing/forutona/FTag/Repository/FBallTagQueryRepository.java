package com.wing.forutona.FTag.Repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.FBallTagResDto;
import com.wing.forutona.FTag.Dto.QTagRankingResDto;
import com.wing.forutona.FTag.Dto.TagRankingResDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.FTag.Domain.QFBalltag.fBalltag;

@Repository
public class FBallTagQueryRepository {

    @PersistenceContext
    EntityManager em;

    public List<FBalltag> findByBallInTags(List<FBall> fBalls){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(fBalltag).from(fBalltag).where(fBalltag.ballUuid.in(fBalls)).fetch();
    }


}
