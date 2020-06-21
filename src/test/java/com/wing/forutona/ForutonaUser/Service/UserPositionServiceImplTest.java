package com.wing.forutona.ForutonaUser.Service;

import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.UserPositionUpdateReqDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class UserPositionServiceImplTest extends BaseTest {

    @Autowired
    UserPositionService userPositionService;

    @MockBean
    FFireBaseToken fFireBaseToken;

    @MockBean
    FUserInfoDataRepository fUserInfoDataRepository;

    @MockBean
    GoogleStorgeAdmin googleStorgeAdmin;

    @Mock
    FUserInfo fUserInfo;


    @BeforeEach
    void setUp() {
        fUserInfo.setUid("testUid");
        given(fUserInfoDataRepository.findById(any())).willReturn(Optional.of(fUserInfo));
    }

    @Test
    void updateUserPosition() {
        //given
        UserPositionUpdateReqDto userPositionUpdateReqDto = new UserPositionUpdateReqDto(120.1,15.0);
        //when
        userPositionService.updateUserPosition(userPositionUpdateReqDto,fFireBaseToken);
        //then
        then(fUserInfoDataRepository).should().findById(any());
        then(fUserInfo).should().updatePlacePoint(any());
    }
}