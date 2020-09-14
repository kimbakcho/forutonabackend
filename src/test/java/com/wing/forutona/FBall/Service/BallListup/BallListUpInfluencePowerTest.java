package com.wing.forutona.FBall.Service.BallListup;

import com.wing.forutona.FBall.Dto.FBallListUpFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.FBall.Service.BallListUpService;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@SpringBootTest
@Transactional
class BallListUpInfluencePowerTest {

    @MockBean
    FBallQueryRepository fBallQueryRepository;

    @MockBean
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Autowired
    BallListUpService ballListUpInfluencePower;

    @Test
    @DisplayName("Ball ListUp 범위 산정 후 영향력 ListUP Repository 호출")
    void search() throws Exception {
        //given
        given(distanceOfBallCountToLimitService.distanceOfBallCountToLimit(any())).willReturn(300);
        given(fBallQueryRepository.getBallListUpFromBallInfluencePower(any(),any(),any())).willReturn(new PageImpl<FBallResDto>(new ArrayList<>()));
        FBallListUpFromBallInfluencePowerReqDto reqDto = new FBallListUpFromBallInfluencePowerReqDto();
        reqDto.setBallLimit(1000);
        reqDto.setLongitude(124.0);
        reqDto.setLatitude(37);
        Pageable pageable = PageRequest.of(0, 20);
        //when
        ballListUpInfluencePower.searchBallListUpInfluencePower(reqDto,pageable);
        //then
        InOrder inOrder = inOrder(distanceOfBallCountToLimitService, fBallQueryRepository);
        then(distanceOfBallCountToLimitService).should(inOrder).distanceOfBallCountToLimit(any());
        then(fBallQueryRepository).should(inOrder).getBallListUpFromBallInfluencePower(any(),any(),any());
    }


}