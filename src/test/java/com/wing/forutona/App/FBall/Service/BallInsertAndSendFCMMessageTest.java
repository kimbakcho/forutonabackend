package com.wing.forutona.App.FBall.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FireBaseMessage.Dto.FireBaseMessageSendDto;
import com.wing.forutona.App.FireBaseMessage.Service.FBallInsertFCMService;
import com.wing.forutona.App.FireBaseMessage.Service.FireBaseMessageAdapter;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Disabled
public class BallInsertAndSendFCMMessageTest extends BaseTest {

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;


    @Autowired
    FireBaseMessageAdapter fireBaseMessageAdapter;

    @Autowired
    FBallInsertFCMService fBallInsertFCMService;


    @Test
    @Disabled
    public void TestFCMSend() throws FirebaseMessagingException, JsonProcessingException, ParseException {
        FBall fBall = fBallDataRepository.findById("9597a088-6761-4ebb-92d1-2f78d0b8ebce").get();
        fBallInsertFCMService.sendInsertFCMMessage(fBall);
    }
}
