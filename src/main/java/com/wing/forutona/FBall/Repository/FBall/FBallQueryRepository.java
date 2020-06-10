package com.wing.forutona.FBall.Repository.FBall;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
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
import static com.wing.forutona.FTag.Domain.QFBalltag.fBalltag;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FBallQueryRepository extends Querydsl4RepositorySupport {

    @PersistenceContext
    EntityManager em;


    public FBallQueryRepository() {
        super(FBall.class);
    }


    /**
     * Front 로 부터 화면의 검색 범위를 좌표로 받음 다음 검색 해줌.
     *
     * @param reqDto
     * @param sorts
     * @param pageable
     * @return
     * @throws ParseException
     */
    public FBallListUpWrapDto getBallListUpFromMapArea(BallFromMapAreaReqDto reqDto,
                                                       MultiSorts sorts, Pageable pageable) throws ParseException {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<OrderSpecifier> orderSpecifiers = PageableUtil.multipleSortToOrders(sorts.getSorts(), fBall);
        String rectStr = GisGeometryUtil.createRectPOLYGONStr(reqDto.getSouthwestLng(),
                reqDto.getSouthwestLat(), reqDto.getNortheastLng(), reqDto.getNortheastLat());
        Geometry rect = new WKTReader().read(rectStr);
        rect.setSRID(4326);
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, rect);
        JPAQuery<FBall> query = queryFactory
                .select(fBall)
                .from(fBall)
                .where(stWithin.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballDeleteFlag.isFalse()
                );
        for (var orderSpecifier : orderSpecifiers) {
            //거리순일때만 분기 처리
            if (isSpecialOrder(orderSpecifier, "distance")) {
                NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class,
                        "function('st_distance_sphere',{0},{1})", fBall.placePoint,
                        GisGeometryUtil.createCenterPoint(reqDto.getCenterPointLat(), reqDto.getCenterPointLng()));
                if (orderSpecifier.getOrder().toString().equals("DESC")) {
                    query = query.orderBy(st_distance_sphere.desc());
                } else {
                    query = query.orderBy(st_distance_sphere.asc());
                }
            } else {
                query = query.orderBy(orderSpecifier);
            }
        }
        List<FBall> fetch = query.limit(pageable.getPageSize())
                .offset(pageable.getOffset()).fetch();
        FBallListUpWrapDto wrapDto = new FBallListUpWrapDto();

        wrapDto.setBalls(fetch.stream().map(x -> new FBallResDto(x)).collect(Collectors.toList()));
        return wrapDto;

    }

    public boolean isSpecialOrder(OrderSpecifier orderSpecifier, String orderType) {
        String[] split = orderSpecifier.getTarget().toString().split("\\.");
        return split.length > 1 && split[1].equals(orderType);
    }



    public FBallListUpWrapDto getBallListUpFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto, MultiSorts sorts, Pageable pageable) throws ParseException {
        List<OrderSpecifier> orderSpecifiers = PageableUtil.multipleSortToOrders(sorts.getSorts(), fBall);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                QFBall.fBall.ballName, "+" + reqDto.getSearchText() + "*");
        JPAQuery<FBall> query = queryFactory
                .select(fBall)
                .from(fBall)
                .where(matchTemplate.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballDeleteFlag.isFalse()
                );
        for (var orderSpecifier : orderSpecifiers) {
            //거리순일때만 분기 처리
            if (isSpecialOrder(orderSpecifier, "distance")) {
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
        wrapDto.setSearchBallTotalCount(fBallQueryResults.getTotal());
        wrapDto.setBalls(fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList()));
        return wrapDto;
    }


    public Long getFindBallCountInDistance(Geometry rect) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, rect);
        Long count = queryFactory.select(fBall.count()).from(fBall)
                .where(stWithin.eq(1), fBall.ballDeleteFlag.isFalse()).fetchOne();
        return count;
    }

    public FBallListUpWrapDto getBallListUpFromBallInfluencePower(Geometry centerPoint, Geometry searchBoundary, Pageable pageable) {
        List<Sort.Direction> sortOrders = pageable.getSort().get()
                .map(x -> x.getDirection()).collect(Collectors.toList());
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, searchBoundary);
            NumberExpression<Double> influence = fBall.ballPower.divide(fBall.placePoint.distance(centerPoint));
            List<FBallResDto> fBallResDtos = queryFactory.select(
                    new QFBallResDto(fBall, ExpressionUtils.as(influence, "Influence")))
                    .from(fBall).join(fBall.fBallUid, fUserInfo)
                    .where(stWithin.eq(1)
                            , fBall.activationTime.after(LocalDateTime.now())
                            , fBall.ballState.eq(FBallState.Play)
                            , fBall.ballDeleteFlag.isFalse()
                    )
                    .orderBy(influence.desc())
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();
            return new FBallListUpWrapDto(LocalDateTime.now(), fBallResDtos);
        }



    public List<UserToMakerBallResDto> getUserToMakerBalls(UserToMakerBallReqDto reqDto,
                                                           MultiSorts sorts, Pageable pageable) {
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
        } else {
            userToMakerBallResDtos = queryFactory.select(new QUserToMakerBallResDto(fBall))
                    .from(fBall)
                    .where(fBall.fBallUid.uid.eq(reqDto.getMakerUid()))
                    .orderBy(orderSpecifiers.get(0))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }

        return userToMakerBallResDtos;
    }


    public FBallListUpWrapDto ListUpFromTagName(FBallListUpFromTagReqDto reqDto,
                                                MultiSorts sorts, Pageable pageable) throws ParseException {
        List<OrderSpecifier> orderSpecifiers = PageableUtil.multipleSortToOrders(sorts.getSorts(), fBall);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        JPAQuery<FBall> query = queryFactory.select(fBall)
                .from(fBalltag)
                .join(fBalltag.ballUuid, fBall)
                .where(fBalltag.tagItem.eq(reqDto.getSearchTag())
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
        wrapDto.setSearchBallTotalCount(fBallQueryResults.getTotal());
        wrapDto.setBalls(fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList()));
        return wrapDto;
    }


    /*
    CenterPoint와 rect 범위 안의 ball 들의 거리 반환
     */
    public List<Tuple> getFindBallInDistanceForQueryDsl(Geometry centerPoint, Geometry rect) {
        NumberTemplate stWithin = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, rect);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> fetch = queryFactory.select(fBall.placePoint,
                fBall.placePoint.distance(centerPoint))
                .from(fBall)
                .where(stWithin.eq(1),
                        fBall.ballDeleteFlag.isFalse())
                .fetch();
        return fetch;
    }




}
