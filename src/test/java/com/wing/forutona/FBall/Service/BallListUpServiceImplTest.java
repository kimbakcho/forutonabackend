package com.wing.forutona.FBall.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Dto.FBallListUpFromBIReqDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.FBallTestUtil;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
class BallListUpServiceImplTest extends BaseTest {

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @MockBean
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Autowired
    BallOfInfluenceCalc ballOfInfluenceCalc;

    @Autowired
    BallListUpService ballListUpService;

    FBallTestUtil fBallTestUtil;
    @BeforeEach
    void beforeEach() throws ParseException {
        fBallTestUtil = new FBallTestUtil(fBallDataRepository,testUser);
        fBallDataRepository.deleteAll();
        when(distanceOfBallCountToLimitService.distanceOfBallCountToLimit(any())).thenReturn(100000);
    }


    @Test
    void searchBallListUpOrderByBI() throws ParseException {
        //given
        LatLng mapCenterPosition = LatLng.newBuilder().setLatitude(37.5012).setLongitude(126.8976).build();
        LatLng userPosition = LatLng.newBuilder().setLatitude(37.5012).setLongitude(126.9203).build();

        fBallTestUtil.makeRandomBallSave(mapCenterPosition,1000,100,true);
        //when
        FBallListUpFromBIReqDto reqDto = new FBallListUpFromBIReqDto();
        reqDto.setMapCenterLatitude(mapCenterPosition.getLatitude());
        reqDto.setMapCenterLongitude(mapCenterPosition.getLongitude());
        reqDto.setUserLatitude(userPosition.getLatitude());
        reqDto.setUserLongitude(userPosition.getLongitude());
        Page<FBallResDto> fBallResDtos = ballListUpService.searchBallListUpOrderByBI(reqDto, PageRequest.of(0, 40));
        for (FBallResDto fBallResDto : fBallResDtos.getContent()) {
            System.out.println("BallUuid =" + fBallResDto.getBallUuid() + "  BI =" + fBallResDto.getBi());
        }
        //then
        assertEquals(100,fBallResDtos.getTotalElements());
        assertEquals(40,fBallResDtos.getContent().size());
        assertTrue(fBallResDtos.isFirst());
        assertTrue(fBallResDtos.getContent().get(0).getBi() >= fBallResDtos.getContent().get(1).getBi());

        //when
        fBallResDtos = ballListUpService.searchBallListUpOrderByBI(reqDto, PageRequest.of(1, 40));

        //then
        assertEquals(100,fBallResDtos.getTotalElements());
        assertEquals(40,fBallResDtos.getContent().size());
        assertFalse(fBallResDtos.isFirst());

        //when
        fBallResDtos = ballListUpService.searchBallListUpOrderByBI(reqDto, PageRequest.of(2, 40));

        //then
        assertEquals(100,fBallResDtos.getTotalElements());
        assertEquals(20,fBallResDtos.getContent().size());
        assertTrue(fBallResDtos.isLast());

    }



}