package com.wing.forutona.FBall.Repository.FBallReply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Domain.QFBallReply;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.FBallReplyResWrapDto;
import com.wing.forutona.FBall.Dto.QFBallReplyResDto;
import com.wing.forutona.ForutonaUser.Domain.QFUserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.wing.forutona.FBall.Domain.QFBallReply.*;
import static com.wing.forutona.ForutonaUser.Domain.QFUserInfo.*;

@Repository
public class FBallReplyQueryRepository {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public int updateReplySortPlusOne(FBall ballUuid,Long replyNumber,Long replySort){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        queryFactory.update(fBallReply).where(fBallReply.replyBallUuid.eq(ballUuid),
                fBallReply.replyNumber.eq(replyNumber),fBallReply.replySort.goe(replySort))
        .set(fBallReply.replySort,fBallReply.replySort.add(1)).execute();
        em.flush();
        em.clear();
        return 1;
    }

    @Transactional
    public FBallReplyResWrapDto getFBallReply(FBall ballUuid, Pageable pageable){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<FBallReplyResDto> fetch = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .join(fBallReply.replyUid,fUserInfo).fetchJoin()
                .where(fBallReply.replyBallUuid.eq(ballUuid),fBallReply.replySort.eq(0L), fBallReply.replyDepth.eq(0L))
                .orderBy(fBallReply.replyUploadDateTime.desc()).limit(pageable.getPageSize()).offset(pageable.getOffset())
                .fetch();

        Long totalReplycount = queryFactory.
                select(fBallReply.count()).from(fBallReply).
                where(fBallReply.replyBallUuid.eq(ballUuid),fBallReply.replySort.eq(0L))
                .fetchOne();

        FBallReplyResWrapDto replyResWrapDto = new FBallReplyResWrapDto();
        replyResWrapDto.setReplyTotalCount(totalReplycount);
        replyResWrapDto.setContents(fetch);
        replyResWrapDto.setCount(fetch.size());
        return replyResWrapDto;
    }

    @Transactional
    public FBallReplyResWrapDto getFBallDetailReply(FBallReplyReqDto reqDto,Pageable pageable) {
        FBall fBall =  FBall.builder().ballUuid(reqDto.getBallUuid()).build();

        FBallReplyResWrapDto fBallReplyResWrapDto = new FBallReplyResWrapDto();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        Long totalReplycount = queryFactory.
                select(fBallReply.count()).from(fBallReply).
                where(fBallReply.replyBallUuid.eq(fBall),fBallReply.replySort.eq(0L))
                .fetchOne();

        fBallReplyResWrapDto.setReplyTotalCount(totalReplycount);

        List<FBallReply> fetch1 = queryFactory.selectFrom(fBallReply).where(fBallReply.replyBallUuid.eq(fBall)
                , fBallReply.replySort.eq(0L)).orderBy(fBallReply.replyUploadDateTime.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .fetch();
        List<Long> replyNumberCollect = fetch1.stream().map(x -> x.getReplyNumber()).collect(Collectors.toList());

        List<FBallReplyResDto> fetch = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .join(fBallReply.replyUid, fUserInfo).fetchJoin()
                .where(fBallReply.replyBallUuid.eq(fBall),
                        fBallReply.replyNumber.in(replyNumberCollect))
                .orderBy(fBallReply.replyNumber.desc())
                .orderBy(fBallReply.replySort.desc()).orderBy(fBallReply.replyDepth.asc()).fetch();

        fBallReplyResWrapDto.setContents(fetch);
        fBallReplyResWrapDto.setCount(fetch.size());


        return fBallReplyResWrapDto;
    }


}
