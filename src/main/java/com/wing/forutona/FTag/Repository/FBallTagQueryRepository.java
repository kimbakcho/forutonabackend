package com.wing.forutona.FTag.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.CustomUtil.PageableUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FTag.Dto.QTagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import com.wing.forutona.FTag.Dto.TagSearchFromTextReqDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.FTag.Domain.QFBalltag.fBalltag;

@Repository
public class FBallTagQueryRepository {

    @PersistenceContext
    EntityManager em;

    /*
    1.centerPoint 으로부터 rect 범위 안의 Ball 들의 파워을 구함
    2.파워와 centerPoint 와 rect 범위 안의 Ball의 거리를 구해서 영향력을 구함.(살아 있는 Ball만 구성 )
    3.해당 영향력을 기준으로 정렬
     */
    public TagRankingWrapdto getFindTagRankingInDistanceOfInfluencePower(Geometry centerPoint, Geometry rect, int limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class, "function('st_distance_sphere',{0},{1})", fBall.placePoint, centerPoint);
        NumberExpression influence = fBall.ballPower.divide(st_distance_sphere).sum();
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, rect);
        List<TagRankingDto> tagRankingDtos = queryFactory.select(
                new QTagRankingDto(fBalltag.tagItem, influence))
                .from(fBall).join(fBall.tags, fBalltag)
                .where(stWithin.eq(1).and(fBall.activationTime.after(LocalDateTime.now())))
                .groupBy(fBalltag.tagItem)
                .orderBy(influence.desc())
                .limit(limit)
                .fetch();
        int i = 1;
        for (TagRankingDto tagRankingDto : tagRankingDtos) {
            tagRankingDto.setRanking(i++);
        }
        TagRankingWrapdto tagRankingWrapdto = new TagRankingWrapdto(LocalDateTime.now(), tagRankingDtos);

        return tagRankingWrapdto;
    }

    /**
     * 검색된 Tag을 사용하여 Ball을 찾을때 사용
     *
     * @param reqDto
     * @param sorts
     * @param pageable
     * @return
     * @throws ParseException GIS WKT를 사용할때 나올수 있음.
     */
    public FBallListUpWrapDto getTagSearchFromTextToBalls(TagSearchFromTextReqDto reqDto,
                                                          MultiSorts sorts, Pageable pageable) throws ParseException {
        List<OrderSpecifier> orderSpecifiers = PageableUtil.multipleSortToOrders(sorts.getSorts(), fBall);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        JPAQuery<FBall> query = queryFactory.select(fBall)
                .from(fBalltag)
                .join(fBalltag.ballUuid, fBall)
                .where(fBalltag.tagItem.eq(reqDto.getSearchText())
                        .and(fBall.activationTime.after(LocalDateTime.now())));
        for (var orderSpecifier : orderSpecifiers) {
            String[] split = orderSpecifier.getTarget().toString().split("\\.");
            //거리순일때만 분기 처리
            if (split.length > 1 && split[1].equals("distance")) {
                NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class,
                        "function('st_distance_sphere',{0},{1})", fBall.placePoint,
                        GisGeometryUtil.createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude()));

                if (orderSpecifier.getOrder().toString().equals("DESC")) {
                    query = query.orderBy(st_distance_sphere.desc());
                } else {
                    query = query.orderBy(st_distance_sphere.asc());
                }
            } else {
                query = query.orderBy(orderSpecifier);
            }
        }
        QueryResults<FBall> fBallQueryResults = query.limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();
        FBallListUpWrapDto wrapDto = new FBallListUpWrapDto();
        wrapDto.setSearchBallCount(fBallQueryResults.getTotal());
        wrapDto.setBalls(fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList()));
        return wrapDto;
    }

    public TagRankingWrapdto getTagSearchFromTextToTagRankings(TagSearchFromTextReqDto reqDto) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                fBalltag.tagItem, "+" + reqDto.getSearchText() + "*");
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
