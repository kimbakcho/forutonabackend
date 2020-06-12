package com.wing.forutona.FBall.Repository.FBallPlayer;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.CustomUtil.FSort;
import com.wing.forutona.CustomUtil.FSorts;

import com.wing.forutona.FBall.Dto.QUserToPlayBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBall.*;
import static com.wing.forutona.FBall.Domain.QFBallPlayer.*;


@Repository
public class FBallPlayerQueryRepository {
    @PersistenceContext
    EntityManager em;

    public List<UserToPlayBallResDto> getUserToPlayBallList(UserToPlayBallReqDto reqDto, FSorts sorts, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory.select(new QUserToPlayBallResDto(fBallPlayer))
                .from(fBallPlayer)
                .join(fBallPlayer.ballUuid, fBall)
                .fetchJoin()
                .where(fBallPlayer.playerUid.uid.eq(reqDto.getPlayerUid()))
                .orderBy(getOrderByFBallWithFBallPlayer(sorts).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();

    }


    public List<OrderSpecifier> getOrderByFBallWithFBallPlayer(FSorts sorts) {
        NumberExpression<Integer> Alive = new CaseBuilder()
                .when(fBallPlayer.ballUuid.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);

        List<OrderSpecifier> orderBys = new LinkedList<>();
        for (FSort sort : sorts.getSorts()) {
            if (sort.equals("startTime")) {
                orderBys.add(sort.toOrderSpecifier(fBallPlayer));
            } else if(sort.equals("Alive")){
                orderBys.add(Alive.desc());
            } else {
                orderBys.add(sort.toOrderSpecifier(fBall));
            }
        }
        return orderBys;
    }


}
