package com.wing.forutona.FireBaseMessage.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FireBaseMessage.Dto.FireBaseMessageSendDto;
import com.wing.forutona.FireBaseMessage.PayloadDto.FCMReplyDto;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public abstract class FBallReplyFCMService {

    @Autowired
    FireBaseMessageAdapter fireBaseMessageAdapter;

    abstract String getFCMToken(FBallReply fBallReply);

    public void sendFCM(FBallReply fBallReply) throws FirebaseMessagingException, JsonProcessingException {
        String fcmToken = getFCMToken(fBallReply);
        FCMReplyDto fcmReplyDto = new FCMReplyDto();
        fcmReplyDto.setReplyUserUid(fBallReply.getReplyUserUid());
        fcmReplyDto.setNickName(fBallReply.getReplyUserNickName());
        fcmReplyDto.setReplyText(fBallReply.getReplyText());
        fcmReplyDto.setUserProfileImageUrl(fBallReply.getReplyUserProfileImageUrl());
        fcmReplyDto.setBallUuid(fBallReply.getBallUuid());
        if(isRootNode(fBallReply)){
            fcmReplyDto.setReplyTitleType("COMMENT");
        }else {
            fcmReplyDto.setReplyTitleType("REPLY");
        }

        FireBaseMessageSendDto replyDtoFireBaseMessageSendDto =
                FireBaseMessageSendDto.builder().commandKey("CommentChannelUseCase")
                        .serviceKey("FBallReplyFCMService")
                        .isNotification(true)
                        .payLoad(fcmReplyDto)
                        .fcmToken(fcmToken)
                        .build();

        fireBaseMessageAdapter.sendMessage(replyDtoFireBaseMessageSendDto);
    }

    public boolean isRootNode(FBallReply fBallReply) {
        return fBallReply.getReplySort()  == 0;
    }

}

@Service("FBallRootReplyFCMService")
@Transactional
class FBallRootReplyFCMService extends FBallReplyFCMService {

    @Override
    String getFCMToken(FBallReply fBallReply) {
        FUserInfo ballMaker = fBallReply.getBallMakerUerInfo();
        return ballMaker.getFCMtoken();
    }
}


@Service("FBallSubReplyFCMService")

@Transactional
class FBallSubReplyFCMService extends FBallReplyFCMService {

    @Autowired
    FBallReplyDataRepository fBallReplyDataRepository;

    @Override
    String getFCMToken(FBallReply fBallReply) {
        FBallReply rootReply = fBallReplyDataRepository.findByReplyBallUuidAndReplyNumberAndReplySort(
                fBallReply.getReplyBallUuid(),
                fBallReply.getReplyNumber(), 0L);

        String fcMtoken = rootReply.getReplyUid().getFCMtoken();
        return fcMtoken;
    }

}