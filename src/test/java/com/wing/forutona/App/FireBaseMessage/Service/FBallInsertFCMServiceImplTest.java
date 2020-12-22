package com.wing.forutona.App.FireBaseMessage.Service;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
class FBallInsertFCMServiceImplTest extends BaseTest {

    @Autowired
    FBallInsertFCMService issueFBallInsertFCMService;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Test
    void sendInsertFCMMessage() throws ParseException {
        //given
        List<FBall> content = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent();
        FBall fBall = content.get(0);
        testUser.updatePlacePoint(fBall.getLatitude(),fBall.getLongitude());
        //when
        issueFBallInsertFCMService.sendInsertFCMMessage(fBall);
        //then
        //핸드폰에서 응답 받는지 체크 필요

    }
}