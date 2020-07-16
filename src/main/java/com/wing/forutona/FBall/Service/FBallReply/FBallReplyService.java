package com.wing.forutona.FBall.Service.FBallReply;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FBallReplyService {

    @Autowired
    FBallReplyDataRepository fBallReplyDataRepository;

    @Autowired
    FBallReplyQueryRepository fBallReplyQueryRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FBallReplyResDto insertFBallReply(FFireBaseToken fireBaseToken
            ,FBallReplyInsertService fBallReplyInsertService
            , FBallReplyInsertReqDto reqDto
            ) {
        FBall fBall = FBall.builder().ballUuid(reqDto.getBallUuid()).build();
        FUserInfo fUserInfo = FUserInfo.builder().uid(fireBaseToken.getUserFireBaseUid()).build();
        FBallReply saveFBallReplyItem = FBallReply.builder()
                .replyUuid(reqDto.getReplyUuid())
                .replyDepth(0L)
                .replyBallUuid(fBall)
                .replyUid(fUserInfo)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyUploadDateTime(LocalDateTime.now())
                .replyText(reqDto.getReplyText())
                .build();
        FBallReply fBallReply = fBallReplyInsertService.insertReply(fireBaseToken, reqDto, saveFBallReplyItem);
        FBallReply saveReply = fBallReplyDataRepository.saveAndFlush(fBallReply);
        FBallReplyResDto fBallReplyResDto = new FBallReplyResDto(saveReply);
        return fBallReplyResDto;
    }


    @Transactional
    public FBallReplyResWrapDto getFBallReply(FBallReplyReqDto reqDto, Pageable pageable) {
        return fBallReplyQueryRepository.getFBallReply(reqDto, pageable);
    }


    @Transactional
    public FBallReplyResDto updateFBallReply(FFireBaseToken fireBaseToken, FBallReplyUpdateReqDto reqDto) throws Throwable {
        FBallReply fBallReply = fBallReplyDataRepository.findById(reqDto.getReplyUuid()).get();
        if (fireBaseToken.getUserFireBaseUid().equals(fBallReply.getReplyUid().getUid())) {
            fBallReply.setReplyText(reqDto.getReplyText());
            fBallReply.setReplyUpdateDateTime(LocalDateTime.now());
        } else {
            throw new Throwable("missMatchUid");
        }
        return new FBallReplyResDto(fBallReply);
    }

    @Transactional
    public FBallReplyResDto deleteFBallReply(FFireBaseToken fireBaseToken, String replyUuid) throws Throwable {
        FBallReply fBallReply = fBallReplyDataRepository.findById(replyUuid).get();
        if (fireBaseToken.getUserFireBaseUid().equals(fBallReply.getReplyUid().getUid())) {
            fBallReply.delete();
        } else {
            throw new Throwable("missMatchUid");
        }
        return new FBallReplyResDto(fBallReply);
    }

}
