package com.wing.forutona.App.FBallReply.Repositroy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallReply.Domain.FBallReply;
import com.wing.forutona.App.FBall.Domain.QFBall;

import com.wing.forutona.App.FBallReply.Domain.QFBallReply;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyResDto;

import com.wing.forutona.App.FBallReply.Dto.QFBallReplyResDto;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallValuationResDto;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static com.wing.forutona.App.FBallReply.Domain.QFBallReply.fBallReply;
import static com.wing.forutona.App.FBallValuation.Domain.QFBallValuation.fBallValuation;


@Repository
public class FBallReplyQueryRepository {

    @PersistenceContext(unitName = "forutonaApp")
    EntityManager em;


    public Long getMaxSortNumber(String rootReplyUuid) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        FBallReply rootFBallReply = queryFactory.select(fBallReply)
                .from(fBallReply)
                .where(fBallReply.replyUuid.eq(rootReplyUuid))
                .fetchOne();

        return queryFactory.select(fBallReply.replySort.max())
                .from(fBallReply)
                .where(fBallReply.replyBallUuid.eq(rootFBallReply.getReplyBallUuid()),
                        fBallReply.replyNumber.eq(rootFBallReply.getReplyNumber()))
                .fetchOne();
    }


    public Long getMaxReplyNumber(String fBallUuid) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        FBall fBall = queryFactory
                .select(QFBall.fBall)
                .from(QFBall.fBall)
                .where(QFBall.fBall.ballUuid.eq(fBallUuid))
                .fetchOne();

        Long itemCount = queryFactory.select(fBallReply.count()).from(fBallReply)
                .where(fBallReply.replyBallUuid.eq(fBall)).fetchOne();
        if (itemCount == 0) {
            return -1L;
        } else {
            return queryFactory.select(fBallReply.replyNumber.max())
                    .from(fBallReply)
                    .where(fBallReply.replyBallUuid.eq(fBall))
                    .fetchOne();
        }
    }

    public Page<FBallReplyResDto> getFBallRootNodeReply(FBall fBall, Pageable pageable) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        OrderSpecifier orderSpecifier;
        orderSpecifier = fBallReply.replyNumber.asc();
        booleanBuilder.and(fBallReply.replySort.eq(0L));

        Page<FBallReplyResDto> fBallReplySearch = getFBallReplySearch(pageable, fBall, booleanBuilder, orderSpecifier);

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        fBallReplySearch.map(x->{
            Long childReplyCount = queryFactory
                    .select(fBallReply.count())
                    .from(fBallReply)
                    .where(fBallReply.replyBallUuid.eq(fBall),
                            fBallReply.replyNumber.eq(x.getReplyNumber()),
                            fBallReply.replySort.ne(0L)).fetchCount();
            x.setChildCount(childReplyCount);
            return x;
        });

        return fBallReplySearch;

    }


    public Page<FBallReplyResDto> getFBallSubNodeReply(FBall fBall, Long replyNumber, Pageable pageable) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        OrderSpecifier orderSpecifier;
        orderSpecifier = fBallReply.replySort.asc();
        booleanBuilder.and(fBallReply.replySort.ne(0L));
        booleanBuilder.and(fBallReply.replyNumber.eq(replyNumber));

        return getFBallReplySearch(pageable, fBall, booleanBuilder, orderSpecifier);
    }

    public Page<FBallReplyResDto> getFBallReplySearch(
            Pageable pageable,
            FBall fBall,
            BooleanBuilder booleanBuilder,
            OrderSpecifier orderSpecifier) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QueryResults<FBallReplyResDto> fBallReplyResDtoQueryResults = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .where(fBallReply.replyBallUuid.eq(fBall), booleanBuilder)
                .orderBy(orderSpecifier).limit(pageable.getPageSize()).offset(pageable.getOffset()).fetchResults();

        fBallReplyResDtoQueryResults.getResults().forEach(x->{
            List<FBallValuation> fBallValuations = queryFactory.select(fBallValuation).from(fBallValuation)
                    .where(fBallValuation.ballUuid.ballUuid.eq(x.getBallUuid().getBallUuid()),
                            fBallValuation.uid.uid.eq(x.getUid().getUid()))
                    .fetch();

            FBallValuationResDto fBallValuationResDto = new FBallValuationResDto();
            fBallValuationResDto.setBallUuid(x.getBallUuid());
            fBallValuationResDto.setValueUuid("SummaryUuid");
            fBallValuations.forEach(fBallValuation1 -> {
                fBallValuationResDto.setPoint(fBallValuationResDto.getPoint() + fBallValuation1.getPoint());
                fBallValuationResDto.setBallLike(fBallValuationResDto.getBallLike() + fBallValuation1.getBallLike());
                fBallValuationResDto.setBallDislike(fBallValuationResDto.getBallDislike() + fBallValuation1.getBallDislike());
            });

            x.setFballValuationResDto(fBallValuationResDto);
        });

        Page<FBallReplyResDto> pageWrap = new PageImpl<FBallReplyResDto>(fBallReplyResDtoQueryResults.getResults(),
                pageable, fBallReplyResDtoQueryResults.getTotal());

        return pageWrap;
    }

    public FBallReply findByIdAndReplyUid (String replyUuid, FUserInfo replyUid){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        FBallReply fBallReply = queryFactory.select(QFBallReply.fBallReply).from(QFBallReply.fBallReply).where(QFBallReply.fBallReply.replyUuid.eq(replyUuid),
                QFBallReply.fBallReply.replyUid.eq(replyUid))
                .fetchOne();
        return fBallReply;
    }

}
