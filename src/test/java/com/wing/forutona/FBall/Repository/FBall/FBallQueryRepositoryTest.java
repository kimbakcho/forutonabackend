package com.wing.forutona.FBall.Repository.FBall;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.FBallTestUtil;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
class FBallQueryRepositoryTest extends BaseTest {

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    FBallTestUtil fBallTestUtil;
    @BeforeEach
    void beforeEach(){
        fBallTestUtil = new FBallTestUtil(fBallDataRepository,testUser);
    }

    @Test()
    @DisplayName("기준 조건의 Ball 갯수 확인 현재 기준은 살아 있는 볼")
    void criteriaBallDistanceRangeCount() throws ParseException {
        //given
        fBallDataRepository.deleteAll();
        LatLng centerPosition = LatLng.newBuilder().setLatitude(37.5088099).setLongitude(126.8912061).build();
        int km = 1000;
        int distance = 10 * km;
        fBallTestUtil.makeRandomBallSave(centerPosition,distance,100,true);
        fBallTestUtil.makeRandomBallSave(centerPosition,distance,20,false);
        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(36.5580266).setLongitude(127.5069707).build();
        fBallTestUtil.makeRandomBallSave(centerPosition2,distance,100,true);

        //when
        long findBallCount = fBallQueryRepository.findByCountIsCriteriaBallFromDistance(centerPosition, distance);
        //then
        assertEquals(100,findBallCount);
    }

    @Test()
    @Disabled
    @DisplayName("기준 조건의 Ball 갯수 확인 현재 기준은 살아 있는 경우 테스트 1보다 범위 증가")
    void criteriaBallDistanceRangeCount2() throws ParseException {
        //given
        fBallDataRepository.deleteAll();
        LatLng centerPosition = LatLng.newBuilder().setLatitude(37.5088099).setLongitude(126.8912061).build();
        int km = 1000;
        int distance = 100 * km;
        fBallTestUtil.makeRandomBallSave(centerPosition,distance,1000,true);

        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(35.429344044107154).setLongitude(128.6334228515625).build();
        fBallTestUtil.makeRandomBallSave(centerPosition2,distance,500,true);
        //when
        long findBallCount = fBallQueryRepository.findByCountIsCriteriaBallFromDistance(centerPosition, distance);
        //then
        assertEquals(1000,findBallCount);
    }





}