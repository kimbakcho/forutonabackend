package com.wing.forutona.ForutonaUser.Service;

import com.wing.forutona.BaseTest;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoQueryRepository;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class FAccountServiceImplTest extends BaseTest {

    @Autowired
    FAccountService fAccountService;

    @MockBean
    FUserInfoQueryRepository fUserInfoQueryRepository;

    @MockBean
    FUserInfoDataRepository fUserInfoDataRepository;

    @MockBean
    GoogleStorgeAdmin googleStorgeAdmin;

    @Test
    @DisplayName("Repository Call 및 토큰 변경 확인 ")
    void updateFireBaseMessageToken() {
        //given
        FUserInfo fUserInfo = new FUserInfo();
        fUserInfo.setUid("test");
        fUserInfo.setFCMtoken("test");
        when(fUserInfoDataRepository.findById(anyString())).thenReturn(Optional.of(fUserInfo));
        //when
        fAccountService.updateFireBaseMessageToken("test","testToken");
        //then
        then(fUserInfoDataRepository).should().findById(anyString());
        assertEquals(fUserInfo.getFCMtoken(),"testToken");

    }
}