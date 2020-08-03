package com.wing.forutona.FTag.Repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FTag.Dto.QTagRankingDto;
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

    public List<TagRankingResDto> getFindTagRankingInDistanceOfInfluencePower(Geometry centerPoint, Geometry rect, int limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class, "function('st_distance_sphere',{0},{1})", fBall.placePoint, centerPoint);

        NumberExpression influence = fBall.ballPower.divide(st_distance_sphere).sum();

        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, rect);

        List<TagRankingResDto> tagRankingResDtos = queryFactory.select(
                new QTagRankingDto(fBalltag.tagItem, influence))
                .from(fBalltag)
                .join(fBall).on(fBalltag.ballUuid.eq(fBall))
                .where(stWithin.eq(1).and(fBall.activationTime.after(LocalDateTime.now())))
                .groupBy(fBalltag.tagItem)
                .orderBy(influence.desc())
                .limit(limit)
                .fetch();

        makeTagRankingIndex(tagRankingResDtos);

        return tagRankingResDtos;
    }

    public void makeTagRankingIndex(List<TagRankingResDto> tagRankingResDtos) {
        int i = 1;
        for (TagRankingResDto tagRankingResDto : tagRankingResDtos) {
            tagRankingResDto.setRanking(i++);
        }
    }


    public List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                fBalltag.tagItem, "+" + searchTag + "*");

        List<TagRankingResDto> tagRankingResDtos = queryFactory.select(
                Projections.bean(TagRankingResDto.class,
                        fBalltag.tagItem.as("tagName"), fBall.ballPower.sum().as("tagBallPower")))
                .from(fBalltag)
                .where(matchTemplate.eq(1)
                        .and(fBall.activationTime.after(LocalDateTime.now())))
                .groupBy(fBalltag.tagItem)
                .orderBy(fBall.ballPower.sum().desc())
                .limit(11).offset(0).fetch();

        makeTagRankingIndex(tagRankingResDtos);

        return tagRankingResDtos;
    }


}
