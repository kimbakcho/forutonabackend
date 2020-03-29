package com.wing.forutona.FTag.Repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FTag.Domain.QFBalltag;
import com.wing.forutona.FTag.Dto.QTagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingReqDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
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


    /*
    1.centerPoint 으로부터 rect 범위 안의 Ball 들의 파워을 구함
    2.파워와 centerPoint 와 rect 범위 안의 Ball의 거리를 구해서 영향력을 구함.
    3.해당 영향력을 기준으로 정렬
     */
    public TagRankingWrapdto getFindTagRankingInDistanceOfInfluencePower(Geometry centerPoint, Geometry rect, int limit){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberExpression influence = fBall.ballPower.divide(fBall.placePoint.distance(centerPoint)).sum();
        List<TagRankingDto> tagRankingDtos = queryFactory.select(
                new QTagRankingDto(fBalltag.tagItem, influence))
                .from(fBall).join(fBall.tags,fBalltag)
                .where(fBall.placePoint.within(rect))
                .groupBy(fBalltag.tagItem)
                .orderBy(influence.desc())
                .limit(limit)
                .fetch();
        int i=1;
        for (TagRankingDto tagRankingDto : tagRankingDtos) {
            tagRankingDto.setRanking(i++);
        }
        TagRankingWrapdto tagRankingWrapdto = new TagRankingWrapdto(LocalDateTime.now(),tagRankingDtos);

        return tagRankingWrapdto;
    }

}
