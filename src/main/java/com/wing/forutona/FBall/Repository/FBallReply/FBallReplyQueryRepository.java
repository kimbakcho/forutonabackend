package com.wing.forutona.FBall.Repository.FBallReply;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBallValuation;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.QFBallReplyResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.wing.forutona.FBall.Domain.QFBallReply.fBallReply;
import static com.wing.forutona.FBall.Domain.QFBallValuation.*;

@Repository
public class FBallReplyQueryRepository {
    @PersistenceContext
    EntityManager em;


    public Long getMaxSortNumber(Long replyNumber, String fBallUuid) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        FBall fBall = FBall.builder().ballUuid(fBallUuid).build();

        return queryFactory.select(fBallReply.replySort.max())
                .from(fBallReply)
                .where(fBallReply.replyBallUuid.eq(fBall), fBallReply.replyNumber.eq(replyNumber))
                .fetchOne();
    }


    public Long getMaxReplyNumber(String fBallUuid) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        FBall fBall = FBall.builder().ballUuid(fBallUuid).build();
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

    public Page<FBallReplyResDto> getFBallRootNodeReply(FBallReplyReqDto reqDto, Pageable pageable) {

        FBall fBall = FBall.builder().ballUuid(reqDto.getBallUuid()).build();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        OrderSpecifier orderSpecifier;
        orderSpecifier = fBallReply.replyNumber.desc();
        booleanBuilder.and(fBallReply.replySort.eq(0L));

        return getFBallReplySearch(pageable, fBall, booleanBuilder, orderSpecifier);
    }


    public Page<FBallReplyResDto> getFBallSubNodeReply(FBallReplyReqDto reqDto, Pageable pageable) {


        FBall fBall = FBall.builder().ballUuid(reqDto.getBallUuid()).build();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        OrderSpecifier orderSpecifier;
        orderSpecifier = fBallReply.replySort.asc();
        booleanBuilder.and(fBallReply.replySort.ne(0L));
        booleanBuilder.and(fBallReply.replyNumber.eq(reqDto.getReplyNumber()));

        return getFBallReplySearch(pageable, fBall, booleanBuilder, orderSpecifier);
    }

    public Page<FBallReplyResDto> getFBallReplySearch(
                                                      Pageable pageable,
                                                      FBall fBall,
                                                      BooleanBuilder booleanBuilder,
                                                      OrderSpecifier orderSpecifier) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QueryResults<FBallReplyResDto> fBallReplyResDtoQueryResults = queryFactory.select(new QFBallReplyResDto(fBallReply,fBallValuation))
                .from(fBallReply)
                .leftJoin(fBallValuation)
                .on(fBallReply.replyBallUuid.eq(fBallValuation.ballUuid))
                .on(fBallReply.replyUid.eq(fBallValuation.uid))
                .where(fBallReply.replyBallUuid.eq(fBall), booleanBuilder)
                .orderBy(orderSpecifier).limit(pageable.getPageSize()).offset(pageable.getOffset()).fetchResults();

        Page<FBallReplyResDto> pageWrap = new PageImpl<FBallReplyResDto>(fBallReplyResDtoQueryResults.getResults(),
                pageable, fBallReplyResDtoQueryResults.getTotal());

        return pageWrap;
    }

}
