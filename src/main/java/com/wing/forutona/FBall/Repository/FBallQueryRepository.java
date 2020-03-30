package com.wing.forutona.FBall.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Dto.FBallState;
import com.wing.forutona.FBall.Dto.QFBallResDto;
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
                    new QFBallResDto(fBall.latitude
                            , fBall.longitude
                            , fBall.ballUuid
                            , fBall.ballName
                            , fBall.ballState
                            , fBall.placeAddress
                            , fBall.ballLikes
                            , fBall.ballDisLikes
                            , fBall.commentCount
                            , fBall.ballPower
                            , fBall.activationTime
                            , fBall.makeTime
                            , fBall.description
                            , fUserInfo.nickName
                            , fUserInfo.profilePicktureUrl
                            , fUserInfo.uid
                            , fUserInfo.userLevel
                            , ExpressionUtils.as(influence, "Influence")))
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

    /*
    CenterPoint와 rect 범위 안의 ball 들의 거리 반환
     */
//    public List<FBallResDto> getFindBallInDistanceForQueryDsl(Geometry centerPoint, Geometry rect) {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//        List<FBallResDto> fetch = queryFactory.select(new QFBallResDto(fBall.placePoint,
//                fBall.placePoint.distance(centerPoint)))
//                .from(fBall)
//                .where(fBall.placePoint.within(rect))
//                .fetch();
//        return fetch;
//    }


}
