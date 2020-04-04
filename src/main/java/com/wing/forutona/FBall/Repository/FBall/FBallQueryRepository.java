package com.wing.forutona.FBall.Repository.FBall;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.FBall.Domain.QFBallPlayer.fBallPlayer;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FBallQueryRepository extends Querydsl4RepositorySupport {

    @PersistenceContext
    EntityManager em;

    public FBallQueryRepository() {
        super(FBall.class);
    }

    /*
     ** 해당 Rect 내에 포함된 Ball의 갯수
     */
    public Long getFindBallCountInDistance(Geometry rect) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Long count = queryFactory.select(fBall.count()).from(fBall)
                .where(fBall.placePoint.within(rect)).fetchOne();
        return count;
    }

    /*
        Ball을 리스트 업 할때 영향력만 예외처리하여 계산 해줌.
     */
    public FBallListUpWrapDto getBallListUp(Geometry centerPoint, Geometry rect, Pageable pageable) {
        List<String> sortProperty = pageable.getSort().get()
                .map(x -> x.getProperty()).collect(Collectors.toList());
        List<Sort.Direction> sortOrders = pageable.getSort().get()
                .map(x -> x.getDirection()).collect(Collectors.toList());
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        if (sortProperty.size() > 0 && sortProperty.contains("Influence")) {
            NumberExpression<Double> influence = fBall.ballPower.divide(fBall.placePoint.distance(centerPoint));
            List<FBallResDto> fBallResDtos = queryFactory.select(
                    new QFBallResDto(fBall, ExpressionUtils.as(influence, "Influence")))
                    .from(fBall).join(fBall.fBallUid, fUserInfo)
                    .where(fBall.placePoint.within(rect)
                            , fBall.activationTime.after(LocalDateTime.now())
                            , fBall.ballState.eq(FBallState.Play))
                    .orderBy(sortOrders.get(0).isDescending() ? influence.desc() : influence.asc())
                    .limit(pageable.getPageSize())
                    .offset(pageable.getOffset())
                    .fetch();
            return new FBallListUpWrapDto(LocalDateTime.now(), fBallResDtos);
        } else {
            return null;
        }
    }

    public List<UserToMakerBallResDto> getUserToMakerBalls(UserToMakerBallReqDto reqDto,Pageable pageable){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<String> sortProperty = pageable.getSort().get()
                .map(x -> x.getProperty()).collect(Collectors.toList());
        NumberExpression<Integer> Alive = new CaseBuilder()
                .when(fBall.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);
        List<OrderSpecifier> orderBys = new LinkedList<>();

        orderBys = PageableUtil.pageAbleToOrders(orderBys, pageable, fBall);

        List<UserToMakerBallResDto> userToMakerBallResDtos = null;
        if (sortProperty.size() > 0 && sortProperty.contains("Alive")) {
            userToMakerBallResDtos = queryFactory.select(new QUserToMakerBallResDto(fBall))
                    .from(fBall)
                    .where(fBall.fBallUid.uid.eq(reqDto.getMakerUid()))
                    .orderBy(Alive.desc()).orderBy(orderBys.get(1))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }else {
            userToMakerBallResDtos = queryFactory.select(new QUserToMakerBallResDto(fBall))
                    .from(fBall)
                    .where(fBall.fBallUid.uid.eq(reqDto.getMakerUid()))
                    .orderBy(orderBys.get(1))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }

        return userToMakerBallResDtos;
    }



    /*
    CenterPoint와 rect 범위 안의 ball 들의 거리 반환
     */
    public List<Tuple> getFindBallInDistanceForQueryDsl(Geometry centerPoint, Geometry rect) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> fetch = queryFactory.select(fBall.placePoint,
                fBall.placePoint.distance(centerPoint))
                .from(fBall)
                .where(fBall.placePoint.within(rect))
                .fetch();
        return fetch;
    }


}
