package com.wing.forutona.App.FireBaseMessage.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallReply.Domain.FBallReply;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
class FBallRootReplyFCMServiceTest extends BaseTest {

    @Autowired
    @Qualifier("FBallRootReplyFCMService")
    FBallReplyFCMService fBallReplyFCMService;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;




    @Test
    @Disabled
    void sendFCM() throws FirebaseMessagingException, JsonProcessingException {
        //given
        FUserInfo testReplyUser = fUserInfoDataRepository.findById("usSMKjNv62eJLkXzFpQux8jWqkT2").get();
        FUserInfo testBallUser = fUserInfoDataRepository.findById("h2q2jl3nRPXZ8809Uvi9KdzSss83").get();
        List<FBall> testFBalls = fBallDataRepository.findByUid(testBallUser);
        FBall testBall = testFBalls.get(0);

        FBallReply fBallReply = FBallReply.builder().
                replyUuid("testUUid")
                .replyBallUuid(testBall)
                .replyDepth(0L)
                .replyText("testUtil")
                .replyUid(testReplyUser)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyNumber(0L)
                .replyUploadDateTime(LocalDateTime.now())
                .replySort(0L)
                .build();

        //when
        //TODO 다시 테스트 필요 라이브러리 문제 있음
//        fBallReplyFCMService.sendFCM(fBallReply);
        //then


    }
}