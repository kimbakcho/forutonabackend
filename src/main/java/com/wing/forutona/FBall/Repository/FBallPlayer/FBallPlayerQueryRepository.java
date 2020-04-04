package com.wing.forutona.FBall.Repository.FBallPlayer;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.CustomUtil.PageableUtil;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Domain.QFBallPlayer;
import com.wing.forutona.FBall.Dto.QUserToPlayBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBallPlayer.fBallPlayer;

@Repository
public class FBallPlayerQueryRepository {
    @PersistenceContext
    EntityManager em;

    //Alive 에서 첫번째 정렬,정렬 받은 최신순으로 정렬,
    public List<UserToPlayBallResDto> findFBallPlayerByPlayer(UserToPlayBallReqDto reqDto, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<String> sortProperty = pageable.getSort().get()
                .map(x -> x.getProperty()).collect(Collectors.toList());
        NumberExpression<Integer> Alive = new CaseBuilder()
                .when(fBallPlayer.ballUuid.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);
        QFBall fBall = new QFBall("fBall");
        QFBallPlayer fBallPlayer = new QFBallPlayer("fBallPlayer");
        List<OrderSpecifier> orderBys = new LinkedList<>();
        if (sortProperty.contains("startTime")) {
            orderBys = PageableUtil.pageAbleToOrders(orderBys, pageable, fBallPlayer);
        }
        if (sortProperty.contains("makeTime")) {
            orderBys = PageableUtil.pageAbleToOrders(orderBys, pageable, fBall);
        }
        List<UserToPlayBallResDto> fBallPlayerQueryResults = null;
        if (sortProperty.size() > 0 && sortProperty.contains("Alive")) {
            fBallPlayerQueryResults = queryFactory.select(new QUserToPlayBallResDto(fBallPlayer))
                    .from(fBallPlayer)
                    .join(fBallPlayer.ballUuid, fBall)
                    .fetchJoin()
                    .where(fBallPlayer.playerUid.uid.eq(reqDto.getPlayerUid()))
                    .orderBy(Alive.desc()).orderBy(orderBys.get(1))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        } else {
            fBallPlayerQueryResults = queryFactory.select(new QUserToPlayBallResDto(fBallPlayer))
                    .from(fBallPlayer)
                    .join(fBallPlayer.ballUuid, fBall)
                    .fetchJoin().orderBy(orderBys.get(0))
                    .where(fBallPlayer.playerUid.uid.eq(reqDto.getPlayerUid()))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }
        return fBallPlayerQueryResults;

    }



}
