package com.wing.forutona.FireBaseMessage.Service;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class FBalIInsertFCMServiceImplTest extends BaseTest {

    @Autowired
    @Qualifier("IssueFBalIInsertFCMService")
    FBalIInsertFCMService issueFBalIInsertFCMService;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Test
    void sendInsertFCMMessage() throws ParseException {
        //given
        List<FBall> content = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent();
        FBall fBall = content.get(0);
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(testFireBaseUser).get();
        fUserInfo.updatePlacePoint(fBall.getLatitude(),fBall.getLongitude());
        //when
        issueFBalIInsertFCMService.sendInsertFCMMessage(fBall);
        //then
        //핸드폰에서 응답 받는지 체크 필요

    }
}