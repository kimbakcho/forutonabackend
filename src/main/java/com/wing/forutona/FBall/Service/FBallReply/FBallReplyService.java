package com.wing.forutona.FBall.Service.FBallReply;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.FireBaseMessage.Service.FBallReplyFCMService;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FBallReplyService {

    final FBallReplyDataRepository fBallReplyDataRepository;

    final FBallReplyQueryRepository fBallReplyQueryRepository;

    final FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallReplyListUpFactory fBallReplyListUpFactory;

    final FBallValuationDataRepository fBallValuationDataRepository;


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FBallReplyResDto insertFBallReply(FFireBaseToken fireBaseToken
            , FBallReplyInsertService fBallReplyInsertService
            ,FBallReplyFCMService fBallReplyFCMService
            , FBallReplyInsertReqDto reqDto
    ) throws FirebaseMessagingException, JsonProcessingException {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        FUserInfo replyUser = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();

        FBallReply saveFBallReplyItem = FBallReply.builder()
                .replyUuid(UUID.randomUUID().toString())
                .replyDepth(0L)
                .replyBallUuid(fBall)
                .replyUid(replyUser)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyUploadDateTime(LocalDateTime.now())
                .replyText(reqDto.getReplyText())
                .build();

        FBallReply fBallReply = fBallReplyInsertService.insertReply(fireBaseToken, reqDto, saveFBallReplyItem);
        FBallReply saveReply = fBallReplyDataRepository.save(fBallReply);
        FBallValuation fBallValuation = null;

        Optional<FBallValuation> valuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, replyUser);
        if(valuationOptional.isPresent()){
            fBallValuation = valuationOptional.get();
        }

        FBallReplyResDto fBallReplyResDto = new FBallReplyResDto(saveReply,fBallValuation);


        fBallReplyFCMService.sendFCM(saveReply);
        return fBallReplyResDto;
    }


    @Transactional
    public Page<FBallReplyResDto> getFBallReply(FBallReplyReqDto reqDto, Pageable pageable) {
        return fBallReplyListUpFactory.getListUpService(reqDto.isReqOnlySubReply()).listUpReply(reqDto,pageable);
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
        FBall fBall = fBallDataRepository.findById(fBallReply.getBallUuid()).get();
        FUserInfo replyUser = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();
        FBallValuation fBallValuation = null;
        Optional<FBallValuation> valuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, replyUser);
        if(valuationOptional.isPresent()){
            fBallValuation = valuationOptional.get();
        }
        return new FBallReplyResDto(fBallReply,fBallValuation);
    }

    @Transactional
    public FBallReplyResDto deleteFBallReply(FFireBaseToken fireBaseToken, String replyUuid) throws Throwable {
        FBallReply fBallReply = fBallReplyDataRepository.findById(replyUuid).get();
        if (fireBaseToken.getUserFireBaseUid().equals(fBallReply.getReplyUid().getUid())) {
            fBallReply.delete();
        } else {
            throw new Throwable("missMatchUid");
        }
        return new FBallReplyResDto(fBallReply,null);
    }

    public Long getFBallReplyCount(String ballUuid) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        return fBallReplyDataRepository.countByReplyBallUuid(fBall);
    }
}
