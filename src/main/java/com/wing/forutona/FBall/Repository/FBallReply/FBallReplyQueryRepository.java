package com.wing.forutona.FBall.Repository.FBallReply;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.FBallReplyResWrapDto;
import com.wing.forutona.FBall.Dto.QFBallReplyResDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBallReply.fBallReply;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.fUserInfo;

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

        List<FBallReplyResDto> fBallReplyResDto = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .join(fBallReply.replyUid, fUserInfo).fetchJoin()
                .where(fBallReply.replyBallUuid.eq(fBall), booleanBuilder)
                .orderBy(orderSpecifier).limit(pageable.getPageSize()).offset(pageable.getOffset())
                .fetch();


        fBallReplyResDto.forEach(item -> {
            if (item.getReplySort() == 0) {
                Long subReplyCount = queryFactory.select(fBallReply.count()).from(fBallReply)
                        .where(fBallReply.replyBallUuid.eq(fBall), fBallReply.replyNumber.eq(item.getReplyNumber()), fBallReply.replySort.ne(0L))
                        .fetchOne();
                item.setSubReplyCount(subReplyCount);
            }
        });

        Long totalReplyCount = queryFactory.
                select(fBallReply.count()).from(fBallReply).
                where(fBallReply.replyBallUuid.eq(fBall), fBallReply.replySort.eq(0L))
                .fetchOne();

        FBallReplyResWrapDto replyResWrapDto = new FBallReplyResWrapDto();
        replyResWrapDto.setReplyTotalCount(totalReplyCount);
        replyResWrapDto.setContents(fBallReplyResDto);
        replyResWrapDto.setCount(fBallReplyResDto.size());
        replyResWrapDto.setOffset(pageable.getOffset());
        replyResWrapDto.setPageSize(pageable.getPageSize());
        replyResWrapDto.setOnlySubReply(reqDto.isReqOnlySubReply());
        return replyResWrapDto;
    }


}
