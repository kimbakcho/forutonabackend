package com.wing.forutona.App.FBallReply.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.App.FBallValuation.Dto.FBallValuationResDto;
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
import java.util.List;
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

        List<FBallValuation> valuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUid(fBall, replyUser);
        FBallReplyResDto fBallReplyResDto = new FBallReplyResDto(saveReply);
        if(valuationOptional.size()>0){
            fBallValuation = valuationOptional.get(0);
            fBallReplyResDto.setFballValuationResDto(new FBallValuationResDto(fBallValuation));
        }

        fBall.setReplyCount(fBall.getReplyCount()+1);

        fBallReplyFCMService.sendFCM(saveReply);
        return fBallReplyResDto;
    }


    @Transactional
    public Page<FBallReplyResDto> getFBallReply(FBallReplyReqDto reqDto, Pageable pageable) {
        return fBallReplyListUpFactory.getListUpService(reqDto.isReqOnlySubReply()).listUpReply(reqDto,pageable);
    }

    @Transactional
    public FBallReplyResDto updateFBallReply(UserAdapter userAdapter, FBallReplyUpdateReqDto reqDto) throws Throwable {
        FBallReply fBallReply = fBallReplyQueryRepository.findByIdAndReplyUid(reqDto.getReplyUuid(),userAdapter.getfUserInfo());
        fBallReply.setReplyText(reqDto.getReplyText());
        fBallReply.setReplyUpdateDateTime(LocalDateTime.now());
        FBall fBall = fBallDataRepository.findById(fBallReply.getBallUuid()).get();
        FUserInfo replyUser = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();
        FBallValuation fBallValuation = null;
        FBallReplyResDto fBallReplyResDto = new FBallReplyResDto(fBallReply);
        List<FBallValuation> valuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUid(fBall, replyUser);
        if(valuationOptional.size()>0){
            fBallValuation = valuationOptional.get(0);
            fBallReplyResDto.setFballValuationResDto(new FBallValuationResDto(fBallValuation));
        }

        return fBallReplyResDto;
    }

    @Transactional
    public FBallReplyResDto deleteFBallReply(UserAdapter userAdapter, String replyUuid) throws Throwable {
        FBallReply fBallReply = fBallReplyQueryRepository.findByIdAndReplyUid(replyUuid,userAdapter.getfUserInfo());
        fBallReply.delete();
        return new FBallReplyResDto(fBallReply);
    }

    public Long getFBallReplyCount(String ballUuid) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        return fBallReplyDataRepository.countByReplyBallUuid(fBall);
    }
}
