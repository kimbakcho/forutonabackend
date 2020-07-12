package com.wing.forutona.FBall.Repository.FBallReply;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Domain.QFBallReply;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBallReply.*;
import static com.wing.forutona.FBall.Domain.QFBallReply.fBallReply;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FBallReplyQueryRepository {
    @PersistenceContext
    EntityManager em;


    public int updateReplySortPlusOne(FBall ballUuid, Long replyNumber, Long replySort) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        queryFactory.update(fBallReply).where(fBallReply.replyBallUuid.eq(ballUuid),
                fBallReply.replyNumber.eq(replyNumber), fBallReply.replySort.goe(replySort))
                .set(fBallReply.replySort, fBallReply.replySort.add(1)).execute();
        em.flush();
        em.clear();
        return 1;
    }



    public Long getMaxSortNumber(Long replyNumber, String fBallUuid) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        FBall fBall = FBall.builder().ballUuid(fBallUuid).build();

        return queryFactory.select(fBallReply.replySort.max())
                .from(fBallReply)
                .where(fBallReply.replyBallUuid.eq(fBall),fBallReply.replyNumber.eq(replyNumber))
                .fetchOne();
    }


    public Long getMaxReplyNumber(String fBallUuid) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        FBall fBall = FBall.builder().ballUuid(fBallUuid).build();
        return queryFactory.select(fBallReply.replyNumber.max())
                .from(fBallReply)
                .where(fBallReply.replyBallUuid.eq(fBall))
                .fetchOne();
    }


    public FBallReplyResWrapDto getFBallReply(FBallReplyReqDto reqDto, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        FBall fBall = FBall.builder().ballUuid(reqDto.getBallUuid()).build();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        OrderSpecifier orderSpecifier;

        if (reqDto.isReqOnlySubReply()) {
            orderSpecifier = fBallReply.replySort.asc();
            booleanBuilder.and(fBallReply.replySort.ne(0L));
            booleanBuilder.and(fBallReply.replyNumber.eq(reqDto.getReplyNumber()));
        } else {
            orderSpecifier = fBallReply.replyNumber.desc();
            booleanBuilder.and(fBallReply.replySort.eq(0L));
        }

        List<FBallReplyResDto> fetch = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .join(fBallReply.replyUid, fUserInfo).fetchJoin()
                .where(fBallReply.replyBallUuid.eq(fBall), booleanBuilder)
                .orderBy(orderSpecifier).limit(pageable.getPageSize()).offset(pageable.getOffset())
                .fetch();

        Long totalReplyCount = queryFactory.
                select(fBallReply.count()).from(fBallReply).
                where(fBallReply.replyBallUuid.eq(fBall), fBallReply.replySort.eq(0L))
                .fetchOne();

        FBallReplyResWrapDto replyResWrapDto = new FBallReplyResWrapDto();
        replyResWrapDto.setReplyTotalCount(totalReplyCount);
        replyResWrapDto.setContents(fetch);
        replyResWrapDto.setCount(fetch.size());
        return replyResWrapDto;
    }


}
