package com.wing.forutona.FBall.Repository.FBall;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.CustomUtil.PageableUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.Querydsl4RepositorySupport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FBallQueryRepository extends Querydsl4RepositorySupport {

    @PersistenceContext
    EntityManager em;


    public FBallQueryRepository() {
        super(FBall.class);
    }

    /**
     * SerachText 기준으로 BallUp을 한다.
     * Frount 에서 검색어로 Ball을 찾는데 주로 이용한다.
     * @return
     */
    public FBallListUpWrapDto getListUpBallFromSearchText(BallNameSearchReqDto reqDto, MultiSorts sorts, Pageable pageable) throws ParseException {
        List<OrderSpecifier> orderSpecifiers = PageableUtil.multipleSortToOrders(sorts.getSorts(), fBall);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                QFBall.fBall.ballName, "+"+reqDto.getSearchText()+"*");
        JPAQuery<FBall> query = queryFactory
                .select(fBall)
                .from(fBall)
                .where(matchTemplate.eq(1)
                        .and(fBall.activationTime.after(LocalDateTime.now())));
        for (var orderSpecifier : orderSpecifiers) {
            String[] split = orderSpecifier.getTarget().toString().split("\\.");
            //거리순일때만 분기 처리
            if(split.length > 1 && split[1].equals("distance")){
                var distance = fBall.placePoint.distance(GisGeometryUtil.createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude()));
                if(orderSpecifier.getOrder().toString().equals("DESC")){
                    query = query.orderBy(distance.desc());
                }else {
                    query = query.orderBy(distance.asc());
                }
            }else {
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


    /**
     * 범위내 Ball 갯수 카운터
     * 주 사용은 목적은 지도 기반 검색에 BallListUp 최적화
     * example 최대 1000개의 볼 까지의 거리 까지만 Sort
     * @param rect
     * @return
     */
    public Long getFindBallCountInDistance(Geometry rect) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class,"function('st_within',{0},{1})", fBall.placePoint, rect);
        Long count = queryFactory.select(fBall.count()).from(fBall)
                .where(stWithin.eq(1)).fetchOne();
        return count;
    }

    /**
     * Ball을 ListUp 할때 Map 중심 위치를 기준으로 검색
     * @param centerPoint Map중심 위치
     * @param rect 검색 범위
     * @param pageable
     * @return
     */
    public FBallListUpWrapDto getBallListUp(Geometry centerPoint, Geometry rect, Pageable pageable) {
        List<String> sortProperty = pageable.getSort().get()
                .map(x -> x.getProperty()).collect(Collectors.toList());
        List<Sort.Direction> sortOrders = pageable.getSort().get()
                .map(x -> x.getDirection()).collect(Collectors.toList());
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class,"function('st_within',{0},{1})", fBall.placePoint, rect);
        if (sortProperty.size() > 0 && sortProperty.contains("Influence")) {
            NumberExpression<Double> influence = fBall.ballPower.divide(fBall.placePoint.distance(centerPoint));
            List<FBallResDto> fBallResDtos = queryFactory.select(
                    new QFBallResDto(fBall, ExpressionUtils.as(influence, "Influence")))
                    .from(fBall).join(fBall.fBallUid, fUserInfo)
                    .where(stWithin.eq(1)
                            , fBall.activationTime.after(LocalDateTime.now())
                            , fBall.ballState.eq(FBallState.Play))
                    .orderBy(sortOrders.get(0).isDescending() ? influence.desc() : influence.asc())
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();
            return new FBallListUpWrapDto(LocalDateTime.now(), fBallResDtos);
        } else {
            List<OrderSpecifier> orderSpecifiers = PageableUtil.pageAbleToOrders(pageable, fBall);
            List<FBallResDto> fBallResDtos = queryFactory.select(
                    new QFBallResDto(fBall))
                    .from(fBall).join(fBall.fBallUid, fUserInfo)
                    .where(stWithin.eq(1)
                            , fBall.activationTime.after(LocalDateTime.now())
                            , fBall.ballState.eq(FBallState.Play))
                    .orderBy(PageableUtil.getDynamicOrderSpecifier(orderSpecifiers,0),
                            PageableUtil.getDynamicOrderSpecifier(orderSpecifiers,1),
                            PageableUtil.getDynamicOrderSpecifier(orderSpecifiers,2))
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();
            return new FBallListUpWrapDto(LocalDateTime.now(), fBallResDtos);
        }
    }



    public List<UserToMakerBallResDto> getUserToMakerBalls(UserToMakerBallReqDto reqDto,
                                                           MultiSorts sorts, Pageable pageable){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberExpression<Integer> Alive = new CaseBuilder()
                .when(fBall.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);

        List<OrderSpecifier> orderSpecifiers = PageableUtil.multipleSortToOrders(sorts.getSorts(), fBall);

        List<UserToMakerBallResDto> userToMakerBallResDtos = null;
        if (sorts.getSorts().size() > 0 && sorts.isContain("Alive")) {
            userToMakerBallResDtos = queryFactory.select(new QUserToMakerBallResDto(fBall))
                    .from(fBall)
                    .where(fBall.fBallUid.uid.eq(reqDto.getMakerUid()))
                    .orderBy(Alive.desc()).orderBy(orderSpecifiers.get(1))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }else {
            userToMakerBallResDtos = queryFactory.select(new QUserToMakerBallResDto(fBall))
                    .from(fBall)
                    .where(fBall.fBallUid.uid.eq(reqDto.getMakerUid()))
                    .orderBy(orderSpecifiers.get(0))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }

        return userToMakerBallResDtos;
    }



    /*
    CenterPoint와 rect 범위 안의 ball 들의 거리 반환
     */
    public List<Tuple> getFindBallInDistanceForQueryDsl(Geometry centerPoint, Geometry rect) {
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class,"function('st_within',{0},{1})", fBall.placePoint, rect);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> fetch = queryFactory.select(fBall.placePoint,
                fBall.placePoint.distance(centerPoint))
                .from(fBall)
                .where(stWithin.eq(1))
                .fetch();
        return fetch;
    }


}
