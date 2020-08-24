package com.wing.forutona.FBallPlayer.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.wing.forutona.FBallPlayer.Domain.FBallPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBallPlayer.*;


@Repository
public class FBallPlayerQueryRepository {
    @PersistenceContext
    EntityManager em;

    public Page<FBallPlayer> getUserToPlayBallList(String playerUid, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QueryResults<FBallPlayer> fBallPlayerQueryResults = queryFactory.select(fBallPlayer)
                .from(fBallPlayer)
                .where(fBallPlayer.playerUid.uid.eq(playerUid))
                .orderBy(gerOrderByMake(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetchResults();
        Page<FBallPlayer> results = new PageImpl<FBallPlayer>(fBallPlayerQueryResults.getResults()
                ,pageable,fBallPlayerQueryResults.getTotal());
        return results;

    }


    public List<OrderSpecifier> gerOrderByMake(Sort sorts) {
        NumberExpression<Integer> Alive = new CaseBuilder()
                .when(fBallPlayer.ballUuid.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);
        List<Sort.Order> collect = sorts.get().collect(Collectors.toList());
        List<OrderSpecifier> orderBys = new LinkedList<>();
        for (Sort.Order sort : collect) {
            if (sort.getProperty().equals("startTimeDESCAliveDESC")) {
                orderBys.add(fBallPlayer.startTime.desc());
                orderBys.add(Alive.desc());
            }
        }
        return orderBys;
    }

}
