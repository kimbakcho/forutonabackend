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

@Transactional
@Disabled
class FBallReplyFCMServiceTest extends BaseTest {

    @Autowired
    @Qualifier("FBallRootReplyFCMService")
    FBallReplyFCMService fBallRootReplyFCMService;

    @Autowired
    @Qualifier("FBallSubReplyFCMService")
    FBallReplyFCMService fBallSubReplyFCMService;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;


    @Test
    @Disabled
    void sendRootReplyFCM() throws FirebaseMessagingException, JsonProcessingException {
        //given
        FUserInfo testReplyUser = fUserInfoDataRepository.findById("hYNJ4omLHQWRqAK9JyIdTjdKcIR2").get();

        FBall fBall = fBallDataRepository.findById("fa7f8330-4e63-4d3d-a9e6-eb8c664d97b9").get();


        FBallReply fBallReply = FBallReply.builder().
                replyUuid("testUUid")
                .replyBallUuid(fBall)
                .replyDepth(0L)
                .replyText("testUtil")
                .replyUid(testReplyUser)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyNumber(0L)
                .replyUploadDateTime(LocalDateTime.now())
                .replySort(0L)
                .build();

        //when
        fBallRootReplyFCMService.sendFCM(fBallReply);
        //then

    }

    @Test
    @Disabled
    void sendSubReplyFCM() throws FirebaseMessagingException, JsonProcessingException {
        FUserInfo testReplyUser = fUserInfoDataRepository.findById("hYNJ4omLHQWRqAK9JyIdTjdKcIR2").get();

        FBall fBall = fBallDataRepository.findById("0eb40f6c-c84c-4bf8-a612-818ad7181aa0").get();


        FBallReply fBallReply = FBallReply.builder().
                replyUuid("testUUid")
                .replyBallUuid(fBall)
                .replyDepth(1L)
                .replyText("testUtil")
                .replyUid(testReplyUser)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyNumber(1L)
                .replyUploadDateTime(LocalDateTime.now())
                .replySort(1L)
                .build();

        fBallSubReplyFCMService.sendFCM(fBallReply);

    }
}