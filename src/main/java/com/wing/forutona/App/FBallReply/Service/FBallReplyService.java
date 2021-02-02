package com.wing.forutona.App.FBallReply.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallReply.Domain.FBallReply;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyReqDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyResDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyUpdateReqDto;
import com.wing.forutona.App.FBallReply.Repositroy.FBallReplyDataRepository;
import com.wing.forutona.App.FBallReply.Repositroy.FBallReplyQueryRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.FireBaseMessage.Service.FBallReplyFCMService;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.SpringSecurity.UserAdapter;
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
@Transactional
@RequiredArgsConstructor
public class FBallReplyService {

    final FBallReplyDataRepository fBallReplyDataRepository;

    final FBallReplyQueryRepository fBallReplyQueryRepository;

    final FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallReplyListUpFactory fBallReplyListUpFactory;

    final FBallValuationDataRepository fBallValuationDataRepository;


    public FBallReplyResDto insertFBallReply(UserAdapter userAdapter
            , FBallReplyInsertService fBallReplyInsertService
            , FBallReplyFCMService fBallReplyFCMService
            , FBallReplyInsertReqDto reqDto
    ) throws FirebaseMessagingException, JsonProcessingException {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        FUserInfo replyUser = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();

        FBallReply saveFBallReplyItem = FBallReply.builder()
                .replyUuid(UUID.randomUUID().toString())
                .replyDepth(0L)
                .replyBallUuid(fBall)
                .replyUid(replyUser)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyUploadDateTime(LocalDateTime.now())
                .replyText(reqDto.getReplyText())
                .build();

        FBallReply fBallReply = fBallReplyInsertService.insertReply(userAdapter, reqDto, saveFBallReplyItem);
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
    public FBallReplyResDto updateFBallReply(UserAdapter userAdapter, FBallReplyUpdateReqDto reqDto) throws Throwable {
        FBallReply fBallReply = fBallReplyDataRepository.findById(reqDto.getReplyUuid()).get();
        fBallReply.setReplyText(reqDto.getReplyText());
        fBallReply.setReplyUpdateDateTime(LocalDateTime.now());
        FBall fBall = fBallDataRepository.findById(fBallReply.getBallUuid()).get();
        FUserInfo replyUser = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();
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
