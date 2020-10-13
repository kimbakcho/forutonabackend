package com.wing.forutona.FBall.Repository;

import com.google.type.LatLng;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Service.BallCustomOrderService.BallCustomOrderFactory;
import com.wing.forutona.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.FTag.Domain.QFBalltag.fBalltag;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FBallQueryRepository  {

    final BallCustomOrderFactory ballCustomOrderFactory;

    @PersistenceContext
    EntityManager em;

    public FBallQueryRepository(BallCustomOrderFactory ballCustomOrderFactory) {
        this.ballCustomOrderFactory = ballCustomOrderFactory;
    }

    public  Page<FBallResDto> findByBallListUpFromMapAreaOrderByBP(BallFromMapAreaReqDto reqDto, Pageable pageable) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        Geometry mapAreaGeoRect = new WKTReader().read(GisGeometryUtil.createRectPOLYGONStr(reqDto.getSouthwestLng(),
                reqDto.getSouthwestLat(), reqDto.getNortheastLng(), reqDto.getNortheastLat()));
        mapAreaGeoRect.setSRID(4326);

        NumberTemplate<Integer> st_within = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})",
                fBall.placePoint, mapAreaGeoRect);

        QueryResults<FBall> fBallQueryResults = queryFactory
                .select(fBall)
                .from(fBall)
                .where(st_within.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballDeleteFlag.isFalse()).orderBy(fBall.ballPower.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        Page<FBallResDto> fBallResDtos = new PageImpl<FBallResDto>(
                fBallQueryResults
                        .getResults()
                        .stream()
                        .map(x -> new FBallResDto(x))
                        .collect(Collectors.toList()), pageable, fBallQueryResults.getTotal());

        return fBallResDtos;
    }

    public List<OrderSpecifier> makeOrderSpecifiers(LatLng position, Sort sort) throws ParseException {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        List<Sort.Order> collect = sort.get().collect(Collectors.toList());
        for (var item : collect) {
            orderSpecifiers.addAll(ballCustomOrderFactory.makeOrder(position, item).make());
        }
        return orderSpecifiers;
    }


    public Page<FBallResDto> findByBallListUpFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto, Pageable pageable) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate matchTemplate = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})",
                QFBall.fBall.ballName, "'" + reqDto.getSearchText() + "'");

        LatLng centerLatLng = LatLng.newBuilder().setLatitude(reqDto.getLatitude()).setLongitude(reqDto.getLongitude()).build();

        QueryResults<FBall> fBallQueryResults = queryFactory
                .select(fBall)
                .from(fBall)
                .join(fBall.uid,fUserInfo)
                .where(matchTemplate.eq(1)
                        , fBall.activationTime.after(LocalDateTime.now())
                        , fBall.ballDeleteFlag.isFalse()
                ).orderBy(makeOrderSpecifiers(centerLatLng, pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<FBallResDto> collect = fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList());
        Page<FBallResDto> page = new PageImpl(collect, pageable, fBallQueryResults.getTotal());
        return page;
    }


    public NumberTemplate<Integer> boundaryInBallsFilter(Geometry searchBoundary) {
        return Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, searchBoundary);
    }


    public Page<FBallResDto> getUserToMakerBalls(String makerUid,
                                                 Pageable pageable) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<OrderSpecifier> fBallOrderSpecifier = makeOrderSpecifiers(null, pageable.getSort());

        QueryResults<FBallResDto> fBallResDtoQueryResults = queryFactory.select(new QFBallResDto(fBall))
                .from(fBall)
                .where(fBall.uid.uid.eq(makerUid))
                .orderBy(fBallOrderSpecifier.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetchResults();
        List<FBallResDto> collect = fBallResDtoQueryResults.getResults();
        Page<FBallResDto> page = new PageImpl(collect, pageable, fBallResDtoQueryResults.getTotal());
        return page;
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
                .orderBy(makeOrderSpecifiers(centerLatLng, pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();

        List<FBallResDto> collect = fBallQueryResults.getResults().stream().map(x -> new FBallResDto(x)).collect(Collectors.toList());
        Page<FBallResDto> page = new PageImpl(collect, pageable, fBallQueryResults.getTotal());

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


    public List<FBall> findByCriteriaBallFromDistance(LatLng centerPosition, int distance) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate<Integer> st_within = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, GisGeometryUtil.createDistanceEllipse(centerPosition, distance));

        return queryFactory.select(fBall).from(fBall).where(st_within.eq(1),
                fBall.activationTime.after(LocalDateTime.now()), fBall.ballDeleteFlag.eq(false)).fetch();
    }


    public long findByCountIsCriteriaBallFromDistance(LatLng centerPosition, int distance) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        NumberTemplate<Integer> st_within = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fBall.placePoint, GisGeometryUtil.createDistanceEllipse(centerPosition, distance));

        return queryFactory.select(fBall).from(fBall).where(st_within.eq(1),
                fBall.activationTime.after(LocalDateTime.now()), fBall.ballDeleteFlag.eq(false)).fetchCount();
    }

}
