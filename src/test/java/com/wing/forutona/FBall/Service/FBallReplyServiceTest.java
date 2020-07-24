package com.wing.forutona.FBall.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import com.wing.forutona.FBall.Service.FBallReply.FBallReplyInsertService;
import com.wing.forutona.FBall.Service.FBallReply.FBallReplyService;
import com.wing.forutona.FireBaseMessage.Service.FBallReplyFCMService;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    @Qualifier("FBallReplyRootInsertService")
    FBallReplyInsertService fBallReplyRootInsertService;

    @Autowired
    @Qualifier("FBallReplySubInsertService")
    FBallReplyInsertService fBallReplySubInsertService;

    @MockBean
    @Qualifier("FBallRootReplyFCMService")
    FBallReplyFCMService fBallReplyFCMService;


    @Autowired
    FBallReplyService fBallReplyService;

    @Mock
    FFireBaseToken fireBaseToken;

    @Test
    @DisplayName("Repository call")
    void getFBallReply() {
        //given
        FBallReplyResWrapDto fBallReplyResWrapDto = new FBallReplyResWrapDto();
        when(fBallReplyQueryRepository.getFBallReply(any(), any())).thenReturn(fBallReplyResWrapDto);
        FBallReplyReqDto reqDto = new FBallReplyReqDto();
        Pageable pageable = PageRequest.of(0, 10);
        //when
        fBallReplyService.getFBallReply(reqDto, pageable);
        //then
        verify(fBallReplyQueryRepository).getFBallReply(any(), any());

    }

    @Test
    @DisplayName("main reply insert call")
    void insertMainFBallReply() throws FirebaseMessagingException, JsonProcessingException {
        //given
        FUserInfo fUserInfo = fUserInfoDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        FBall fBall = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        when(fireBaseToken.getUserFireBaseUid()).thenReturn(fUserInfo.getUid());
        FBallReplyInsertReqDto reqDto = new FBallReplyInsertReqDto();
        reqDto.setReplyNumber(-1L);
        reqDto.setBallUuid(fBall.getBallUuid());
        reqDto.setReplyText("test");
        reqDto.setReplyUuid(UUID.randomUUID().toString());
        //when
        FBallReplyResDto fBallReplyResDto = fBallReplyService.insertFBallReply(
                fireBaseToken,
                fBallReplyRootInsertService,
                fBallReplyFCMService, reqDto);
        //then
        assertNotEquals(-1, fBallReplyResDto.getReplyNumber());

    }

    @Test
    @DisplayName("sub reply insert call")
    void insertSubFBallReply() throws FirebaseMessagingException, JsonProcessingException {
        //given
        FUserInfo fUserInfo = fUserInfoDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        FBall fBall = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        FBallReply fBallReply = fBallReplyDataRepository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        List<FBallReply> replyItems = fBallReplyDataRepository.findByReplyBallUuidIsAndReplyNumberIsOrderByReplyUploadDateTimeDesc(
                fBall, fBallReply.getReplyNumber()
        );
        int chapterItemsLength = replyItems.size();
        when(fireBaseToken.getUserFireBaseUid()).thenReturn(fUserInfo.getUid());
        FBallReplyInsertReqDto reqDto = new FBallReplyInsertReqDto();
        reqDto.setReplyNumber(fBallReply.getReplyNumber());
        reqDto.setBallUuid(fBall.getBallUuid());
        reqDto.setReplyText("test");
        reqDto.setReplyUuid(UUID.randomUUID().toString());
        //when
        fBallReplyService.insertFBallReply(fireBaseToken, fBallReplySubInsertService, fBallReplyFCMService, reqDto);
        //then
        List<FBallReply> thenReplyItems = fBallReplyDataRepository.findByReplyBallUuidIsAndReplyNumberIsOrderByReplyUploadDateTimeDesc(
                fBall, fBallReply.getReplyNumber()
        );
        assertEquals(chapterItemsLength + 1, thenReplyItems.size());

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