package com.wing.forutona.FireBaseMessage.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.stereotype.Service;

public interface FBallReplyFCMService {
    void sendFCM(FBallReply fBallReply) throws FirebaseMessagingException;
}

@Service("FBallRootReplyFCMService")
class FBallRootReplyFCMService implements FBallReplyFCMService {

    @Override
    public void sendFCM(FBallReply fBallReply) throws FirebaseMessagingException {
        FUserInfo ballMaker = fBallReply.getBallMakerUerInfo();
        String fcMtoken = ballMaker.getFCMtoken();
        Message message = Message.builder()
                .putData("commandKey","CommentChannelUseCase")
                .putData("serviceKey","FBallRootReplyFCMService")
                .putData("isNotification","true")
                .putData("replyUserUid", fBallReply.getReplyUserUid())
                .putData("nickName", fBallReply.getReplyUserNickName())
                .putData("replyText", fBallReply.getReplyText())
                .putData("userProfileImageUrl", fBallReply.getReplyUserProfileImageUrl())
                .setToken(fcMtoken)
                .build();
        FirebaseMessaging.getInstance().send(message);
    }
}
