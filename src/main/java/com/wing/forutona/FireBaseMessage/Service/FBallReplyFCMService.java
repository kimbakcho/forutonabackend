package com.wing.forutona.FireBaseMessage.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
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
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(fcMtoken)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);

        System.out.println("Successfully sent message: " + response);
    }
}
