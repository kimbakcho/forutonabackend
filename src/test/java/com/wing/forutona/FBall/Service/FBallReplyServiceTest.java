package com.wing.forutona.FBall.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import com.wing.forutona.FBall.Service.FBallReply.FBallReplyService;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
class FBallReplyServiceTest extends BaseTest {

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    FBallReplyDataRepository fBallReplyDataRepository;

    @MockBean
    FBallReplyQueryRepository fBallReplyQueryRepository;


//    @MockBean
//    @Qualifier("FBallRootReplyFCMService")
//    FBallReplyFCMService fBallReplyFCMService;


    @Autowired
    FBallReplyService fBallReplyService;

    @Mock
    FFireBaseToken fireBaseToken;

    @Test
    @DisplayName("Repository call")
    void getFBallReply() {


    }

    @Test
    @DisplayName("main reply insert call")
    void insertMainFBallReply() throws FirebaseMessagingException, JsonProcessingException {

    }

    @Test
    @DisplayName("sub reply insert call")
    void insertSubFBallReply() throws FirebaseMessagingException, JsonProcessingException {
        //given

    }

    @Test
    void updateFBallReply() throws Throwable {
        //given
        FBallReply fBallReply = fBallReplyDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        when(fireBaseToken.getUserFireBaseUid()).thenReturn(fBallReply.getReplyUid().getUid());
        FBallReplyUpdateReqDto reqDto = new FBallReplyUpdateReqDto();
        reqDto.setReplyUuid(fBallReply.getReplyUuid());
        reqDto.setReplyText("testEdit");
        //when
        fBallReplyService.updateFBallReply(fireBaseToken, reqDto);
        //then
        assertEquals("testEdit", fBallReply.getReplyText());
    }

    @Test
    void deleteFBallReply() throws Throwable {
        //given
        FBallReply fBallReply = fBallReplyDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        when(fireBaseToken.getUserFireBaseUid()).thenReturn(fBallReply.getReplyUid().getUid());

        //when
        fBallReplyService.deleteFBallReply(fireBaseToken, fBallReply.getReplyUuid());
        //then
        assertEquals(true, fBallReply.getDeleteFlag());
    }
}