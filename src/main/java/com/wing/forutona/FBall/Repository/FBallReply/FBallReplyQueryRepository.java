package com.wing.forutona.FBall.Repository.FBallReply;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Domain.QFBallReply;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.QFBallReplyResDto;
import com.wing.forutona.ForutonaUser.Domain.QFUserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

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
    public List<FBallReplyResDto> getFBallReply(FBall ballUuid, Pageable pageable){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<FBallReplyResDto> fetch = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .join(fBallReply.replyUid,fUserInfo).fetchJoin()
                .where(fBallReply.replyBallUuid.eq(ballUuid), fBallReply.replyDepth.eq(0L))
                .orderBy(fBallReply.replyNumber.desc()).limit(pageable.getPageSize()).offset(pageable.getOffset())
                .fetch();
        return fetch;
    }

    @Transactional
    public List<FBallReplyResDto> getFBallDetailReply(FBallReplyReqDto reqDto) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<FBallReplyResDto> fetch = queryFactory.select(new QFBallReplyResDto(fBallReply))
                .from(fBallReply)
                .join(fBallReply.replyUid, fUserInfo).fetchJoin()
                .where(fBallReply.replyBallUuid.eq(fBall),
                        fBallReply.replyNumber.eq(reqDto.getReplyNumber()),
                        fBallReply.replyDepth.notIn(0L))
                .orderBy(fBallReply.replySort.asc()).fetch();
        return fetch;
    }
}
