package com.wing.forutona.FBall.Repository.FBall;

import com.google.type.LatLng;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.wing.forutona.CustomUtil.FSort;
import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.Querydsl4RepositorySupport;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    public Page<FBallResDto> getBallListUpFromMapArea(BallFromMapAreaReqDto reqDto,
                                                        Pageable pageable) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        String mapAreaGeoRectStr = GisGeometryUtil.createRectPOLYGONStr(reqDto.getSouthwestLng(),
                reqDto.getSouthwestLat(), reqDto.getNortheastLng(), reqDto.getNortheastLat());
        Geometry mapAreaGeoRect = new WKTReader().read(mapAreaGeoRectStr);
        mapAreaGeoRect.setSRID(4326);

        NumberTemplate stWithin = boundaryInBallsFilter(mapAreaGeoRect);

        LatLng centerLatLng = LatLng.newBuilder().setLatitude(reqDto.getCenterPointLat()).setLongitude(reqDto.getCenterPointLng()).build();

        QueryResults<FBall> fBallQueryResults = queryFactory
                .select(fBall)
                .from(fBall)
                .where(stWithin.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballDeleteFlag.isFalse())
                .orderBy(getDistanceWithOrderSpecifiers(centerLatLng, pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset()).fetchResults();
        List<FBall> results = fBallQueryResults.getResults();
        List<FBallResDto> collect = results.stream().map(x -> new FBallResDto(x)).collect(Collectors.toList());
        Page<FBallResDto> page = new PageImpl<FBallResDto>(collect,pageable,fBallQueryResults.getTotal());;

        return page;
    }

    public List<OrderSpecifier> getDistanceWithOrderSpecifiers(LatLng position, Sort sort) throws ParseException {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        List<Sort.Order> collect = sort.get().collect(Collectors.toList());
        for (var item : collect ) {
                if (item.getProperty().equals("distance")) {
                    NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class,
                            "function('st_distance_sphere',{0},{1})", fBall.placePoint,
                            GisGeometryUtil.createCenterPoint(position.getLatitude(), position.getLongitude()));
                    if (item.getDirection().equals(Order.DESC)) {
                        orderSpecifiers.add(st_distance_sphere.desc());
                    } else {
                        orderSpecifiers.add(st_distance_sphere.asc());
                    }
                } else {
                    PathBuilder<QFBall> entityPath = new PathBuilder(FBall.class, "fBall");
                    PathBuilder<Object> path = entityPath.get(item.getProperty());
                    orderSpecifiers.add(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(item.getDirection().name()), path));
                }
        }
        return orderSpecifiers;
    }


    public Page<FBallResDto> getBallListUpFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto, Pageable pageable) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                QFBall.fBall.ballName, "+" + reqDto.getSearchText() + "*");

        LatLng centerLatLng = LatLng.newBuilder().setLatitude(reqDto.getLatitude()).setLongitude(reqDto.getLongitude()).build();

        QueryResults<FBall> fBallQueryResults = queryFactory
                .select(fBall)
                .from(fBall)
                .where(matchTemplate.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballDeleteFlag.isFalse()
                ).orderBy(getDistanceWithOrderSpecifiers(centerLatLng, pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<FBallResDto> collect = fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList());
        Page<FBallResDto> page = new PageImpl(collect,pageable,fBallQueryResults.getTotal());
        return page;
    }

    public Long getFindBallCountInDistance(Geometry rect) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate stWithin = boundaryInBallsFilter(rect);

        Long count = queryFactory.select(fBall.count()).from(fBall)
                .where(stWithin.eq(1), fBall.ballDeleteFlag.isFalse()).fetchOne();
        return count;
    }

    public Page<FBallResDto> getBallListUpFromBallInfluencePower(Geometry centerPoint, Geometry searchBoundary, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate stWithin = boundaryInBallsFilter(searchBoundary);

        NumberExpression<Double> influence = fBall.ballPower.divide(fBall.placePoint.distance(centerPoint));

        List<FBallResDto> fBallResDtos = queryFactory.select(
                new QFBallResDto(fBall, ExpressionUtils.as(influence, "Influence")))
                .from(fBall).join(fBall.uid, fUserInfo)
                .where(stWithin.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballState.eq(FBallState.Play)
                        , fBall.ballDeleteFlag.isFalse()
                )
                .orderBy(influence.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Page<FBallResDto> page = new PageImpl(fBallResDtos);
        return page;
    }

    public NumberTemplate<Integer> boundaryInBallsFilter(Geometry searchBoundary) {
        return Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, searchBoundary);
    }


    public Page<FBallResDto> getUserToMakerBalls(String makerUid,
                                                            Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<OrderSpecifier> fBallOrderSpecifier = getAliveWithFBallOrderSpecifier(pageable.getSort());
        QueryResults<FBallResDto> fBallResDtoQueryResults = queryFactory.select(new QFBallResDto(fBall))
                .from(fBall)
                .where(fBall.uid.uid.eq(makerUid))
                .orderBy(fBallOrderSpecifier.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetchResults();
        List<FBallResDto> collect = fBallResDtoQueryResults.getResults();
        Page<FBallResDto> page = new PageImpl(collect,pageable,fBallResDtoQueryResults.getTotal());
        return page;
    }

    private List<OrderSpecifier> getAliveWithFBallOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orderBys = new LinkedList<>();
//        for (FSort sort : sorts.getSorts()) {
//            if (sort.getSort().equals("Alive")) {
//                orderBys.add(getAliveCaseBuilder().desc());
//            } else {
//                orderBys.add(sort.toOrderSpecifier(fBall));
//            }
//        }
        return orderBys;
    }


    private NumberExpression<Integer> getAliveCaseBuilder() {
        return new CaseBuilder()
                .when(fBall.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);
    }


    public Page<FBallResDto> ListUpFromTagName(FBallListUpFromTagReqDto reqDto,
                                                 Pageable pageable) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        LatLng centerLatLng = LatLng.newBuilder().setLatitude(reqDto.getLatitude()).setLongitude(reqDto.getLongitude()).build();

        QueryResults<FBall> fBallQueryResults = queryFactory.select(fBall)
                .from(fBalltag)
                .join(fBalltag.ballUuid, fBall)
                .where(fBalltag.tagItem.eq(reqDto.getSearchTag())
                        .and(fBall.activationTime.after(LocalDateTime.now())))
                .orderBy(getDistanceWithOrderSpecifiers(centerLatLng, pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<FBallResDto> collect = fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList());
        Page<FBallResDto> page = new PageImpl(collect,pageable,fBallQueryResults.getTotal());

        return page;
    }


    public List<Tuple> getFindBallInDistanceForQueryDsl(Geometry centerPoint, Geometry rect) {
        NumberTemplate stWithin = boundaryInBallsFilter(rect);

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
