package com.wing.forutona.FBall.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.MapFindScopeStepRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Transactional
class DistanceOfBallCountToLimitServiceImplTest extends BaseTest {

    @Autowired
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Autowired
    MapFindScopeStepRepository mapFindScopeStepRepository;

    @MockBean
    FBallQueryRepository fBallQueryRepository;

    @Test
    @DisplayName("Limit 까지 볼 검색시 해당 거리 리턴 거리는 검색 범위")
    void distanceOfBallCountToLimit() throws ParseException {
        //given
        mapFindScopeStepRepository.deleteAll();
        int km = 1000;
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(0).scopeMeter(10*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(1).scopeMeter(20*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(2).scopeMeter(30*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(3).scopeMeter(40*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(4).scopeMeter(50*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(5).scopeMeter(60*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(6).scopeMeter(70*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(7).scopeMeter(80*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(8).scopeMeter(90*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(9).scopeMeter(100*km).build());

        given(fBallQueryRepository.findByCountIsCriteriaBallFromDistance(any(),anyInt()))
                .willReturn(1L).willReturn(10L).willReturn(100L).willReturn(1000L).willReturn(1200L);
        //when
        int result = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(LatLng.newBuilder().setLatitude(37.0).setLongitude(127.0).build());
        //then
        assertEquals(50*km,result);
    }

    @Test
    @DisplayName("Limit 까지 볼 검색시 해당 거리 리턴 거리는 검색 범위 최대 값가지 정책에 의한 BallLimit 를 채울지 못할때.")
    void distanceOfBallCountToLimitMax() throws ParseException {
        //given
        mapFindScopeStepRepository.deleteAll();
        int km = 1000;
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(0).scopeMeter(10*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(1).scopeMeter(20*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(2).scopeMeter(30*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(3).scopeMeter(40*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(4).scopeMeter(50*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(5).scopeMeter(60*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(6).scopeMeter(70*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(7).scopeMeter(80*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(8).scopeMeter(90*km).build());
        mapFindScopeStepRepository.save(FMapFindScopeStep.builder().idx(9).scopeMeter(100*km).build());

        when(fBallQueryRepository.findByCountIsCriteriaBallFromDistance(any(),anyInt())).thenReturn(10L);
        //when
        int result = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(LatLng.newBuilder().setLatitude(37.0).setLongitude(127.0).build());
        //then
        assertEquals(100*km,result);
    }
}