package com.wing.forutona.FBall.Repository.FBallPlayer;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.CustomUtil.MultiSort;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.CustomUtil.PageableUtil;

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

    //Alive 에서 첫번째 정렬,정렬 받은 최신순으로 정렬,
    public List<UserToPlayBallResDto> getUserToPlayBallList(UserToPlayBallReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberExpression<Integer> Alive = new CaseBuilder()
                .when(fBallPlayer.ballUuid.activationTime.after(LocalDateTime.now()))
                .then(1)
                .otherwise(0);


        List<OrderSpecifier> orderBys = new LinkedList<>();
        for (MultiSort sort : sorts.getSorts()) {
            if (sort.equals("startTime")) {
                PageableUtil.multipleSortToOrders(orderBys, sort, fBallPlayer);
            } else {
                PageableUtil.multipleSortToOrders(orderBys, sort, fBall);
            }
        }

        List<UserToPlayBallResDto> fBallPlayerQueryResults = null;
        if (sorts.getSorts().size() > 0 && sorts.isContain("Alive")) {
            fBallPlayerQueryResults = queryFactory.select(new QUserToPlayBallResDto(fBallPlayer))
                    .from(fBallPlayer)
                    .join(fBallPlayer.ballUuid, fBall)
                    .fetchJoin()
                    .where(fBallPlayer.playerUid.uid.eq(reqDto.getPlayerUid()))
                    .orderBy(Alive.desc(),PageableUtil.getDynamicOrderSpecifier(orderBys,1))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        } else {
            fBallPlayerQueryResults = queryFactory.select(new QUserToPlayBallResDto(fBallPlayer))
                    .from(fBallPlayer)
                    .join(fBallPlayer.ballUuid, fBall)
                    .fetchJoin()
                    .where(fBallPlayer.playerUid.uid.eq(reqDto.getPlayerUid()))
                    .orderBy(PageableUtil.getDynamicOrderSpecifier(orderBys,0),
                            PageableUtil.getDynamicOrderSpecifier(orderBys,1))
                    .limit(pageable.getPageSize()).offset(pageable.getOffset()).fetch();
        }
        return fBallPlayerQueryResults;

    }


}
