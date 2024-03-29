package com.wing.forutona.App.FireBaseMessage.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.App.FBallReply.Domain.FBallReply;
import com.wing.forutona.App.FBallReply.Repositroy.FBallReplyDataRepository;
import com.wing.forutona.App.FireBaseMessage.Dto.FireBaseMessageSendDto;
import com.wing.forutona.App.FireBaseMessage.PayloadDto.FCMReplyDto;
import com.wing.forutona.App.FireBaseMessage.Value.NotificationServiceType;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public abstract class FBallReplyFCMService {

    @Autowired
    FireBaseMessageAdapter fireBaseMessageAdapter;

    abstract String getFCMToken(FBallReply fBallReply);

    public void sendFCM(FBallReply fBallReply) throws FirebaseMessagingException, JsonProcessingException {
        String fcmToken = getFCMToken(fBallReply);

        if(fcmToken == null){
            return ;
        }

        FCMReplyDto fcmReplyDto = new FCMReplyDto();
        fcmReplyDto.setReplyUserUid(fBallReply.getReplyUserUid());
        fcmReplyDto.setNickName(fBallReply.getReplyUserNickName());
        fcmReplyDto.setReplyText(fBallReply.getReplyText());
        fcmReplyDto.setUserProfileImageUrl(fBallReply.getReplyUserProfileImageUrl());
        fcmReplyDto.setBallUuid(fBallReply.getBallUuid());
        fcmReplyDto.setFBallType(fBallReply.getReplyBallUuid().getBallType());
        if(isRootNode(fBallReply)){
            fcmReplyDto.setReplyTitleType("COMMENT");
        }else {
            fcmReplyDto.setReplyTitleType("REPLY");
        }

        FireBaseMessageSendDto replyDtoFireBaseMessageSendDto =
                FireBaseMessageSendDto.builder()
                        .serviceKey(NotificationServiceType.FBallReply.name())
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
@RequiredArgsConstructor
class FBallRootReplyFCMService extends FBallReplyFCMService {

    final FUserInfoDataRepository fUserInfoDataRepository;

    @Override
    String getFCMToken(FBallReply fBallReply) {
        Optional<FUserInfo> ballMakerOptional = fUserInfoDataRepository.findById(fBallReply.getBallMakerUerInfo().getUid());
        System.out.println(fBallReply.getBallMakerUerInfo().getUid());
        if(fBallReply.getBallMakerUerInfo().getUid().equals(fBallReply.getReplyUserUid())){
            return null;
        }
        return ballMakerOptional.get().getFCMtoken();
    }
}


@Service("FBallSubReplyFCMService")
@Transactional
@RequiredArgsConstructor
class FBallSubReplyFCMService extends FBallReplyFCMService {


    final private FBallReplyDataRepository fBallReplyDataRepository;

    final private FUserInfoDataRepository fUserInfoDataRepository;

    @Override
    String getFCMToken(FBallReply fBallReply) {
        FBallReply rootReply = fBallReplyDataRepository.findByReplyBallUuidAndReplyNumberAndReplySort(
                fBallReply.getReplyBallUuid(),
                fBallReply.getReplyNumber(), 0L);

        FUserInfo fUserInfo = fUserInfoDataRepository.findById(rootReply.getReplyUid().getUid()).get();
        if(rootReply.getReplyUid().getUid().equals(fBallReply.getReplyUserUid())){
            return  null;
        }
        return fUserInfo.getFCMtoken();
    }

}