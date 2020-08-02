package com.wing.forutona.FTag.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FTag.Dto.QTagRankingDto;
import com.wing.forutona.FTag.Dto.RelationTagRankingFromTagNameReqDto;
import com.wing.forutona.FTag.Dto.TagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import com.wing.forutona.FBall.Dto.FBallListUpFromTagReqDto;
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

    public TagRankingWrapdto getFindTagRankingInDistanceOfInfluencePower(Geometry centerPoint, Geometry rect, int limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class, "function('st_distance_sphere',{0},{1})", fBall.placePoint, centerPoint);

        NumberExpression influence = fBall.ballPower.divide(st_distance_sphere).sum();

        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, rect);

        List<TagRankingDto> tagRankingDtos = queryFactory.select(
                new QTagRankingDto(fBalltag.tagItem, influence))
                .from(fBall)
                .where(stWithin.eq(1).and(fBall.activationTime.after(LocalDateTime.now())))
                .groupBy(fBalltag.tagItem)
                .orderBy(influence.desc())
                .limit(limit)
                .fetch();

        makeTagRankingIndex(tagRankingDtos);

        TagRankingWrapdto tagRankingWrapdto = new TagRankingWrapdto(LocalDateTime.now(), tagRankingDtos);
        return tagRankingWrapdto;
    }

    public void makeTagRankingIndex(List<TagRankingDto> tagRankingDtos) {
        int i = 1;
        for (TagRankingDto tagRankingDto : tagRankingDtos) {
            tagRankingDto.setRanking(i++);
        }
    }


    public TagRankingWrapdto getRelationTagRankingFromTagNameOrderByBallPower(RelationTagRankingFromTagNameReqDto reqDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                fBalltag.tagItem, "+" + reqDto.getSearchTagName() + "*");
        List<TagRankingDto> fetchlists = queryFactory.select(
                Projections.bean(TagRankingDto.class,
                        fBalltag.tagItem.as("tagName"), fBall.ballPower.sum().as("tagBallPower"))
        )
                .from(fBalltag)
                .join(fBalltag.ballUuid, fBall)
                .where(matchTemplate.eq(1)
                        .and(fBall.activationTime.after(LocalDateTime.now())))
                .groupBy(fBalltag.tagItem)
                .orderBy(fBall.ballPower.sum().desc())
                .limit(11).offset(0).fetch();
        TagRankingWrapdto tagRankingWrapdto = new TagRankingWrapdto();
        tagRankingWrapdto.setContents(fetchlists);
        tagRankingWrapdto.setSearchTime(LocalDateTime.now());
        return tagRankingWrapdto;
    }


}
