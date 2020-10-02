package com.wing.forutona.FBall.Repository.FBall;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.BallFromMapAreaReqDto;
import com.wing.forutona.FBall.FBallTestUtil;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
class FBallQueryRepositoryTest extends BaseTest {

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    FBallTestUtil fBallTestUtil;

    @BeforeEach
    void beforeEach() {
        fBallTestUtil = new FBallTestUtil(fBallDataRepository, testUser);
    }

    @Test()
    @DisplayName("기준 조건의 Ball 갯수 확인 현재 기준은 살아 있는 볼")
    void criteriaBallDistanceRangeCount() throws ParseException {
        //given
        fBallDataRepository.deleteAll();
        LatLng centerPosition = LatLng.newBuilder().setLatitude(37.5088099).setLongitude(126.8912061).build();
        int km = 1000;
        int distance = 10 * km;
        fBallTestUtil.makeRandomBallSave(centerPosition, distance, 100, true);
        fBallTestUtil.makeRandomBallSave(centerPosition, distance, 20, false);
        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(36.5580266).setLongitude(127.5069707).build();
        fBallTestUtil.makeRandomBallSave(centerPosition2, distance, 100, true);

        //when
        long findBallCount = fBallQueryRepository.findByCountIsCriteriaBallFromDistance(centerPosition, distance);
        //then
        assertEquals(100, findBallCount);
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
        fBallTestUtil.makeRandomBallSave(centerPosition, distance, 1000, true);

        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(35.429344044107154).setLongitude(128.6334228515625).build();
        fBallTestUtil.makeRandomBallSave(centerPosition2, distance, 500, true);
        //when
        long findBallCount = fBallQueryRepository.findByCountIsCriteriaBallFromDistance(centerPosition, distance);
        //then
        assertEquals(1000, findBallCount);
    }


    @Test
    @DisplayName("사각형 영역안에 들어 있는 볼만 검색 (살아 있고 지워지지 않는 볼만)")
    void getBallListUpFromMapAreaTEST() throws ParseException {
        //given
        fBallDataRepository.deleteAll();
        LatLng centerPosition = LatLng.newBuilder().setLatitude(37.5088099).setLongitude(126.8912061).build();
        int km = 1000;
        int distance = 45 * km;
        fBallTestUtil.makeRandomBallSave(centerPosition, distance, 1000, true);

        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(35.285984736065764).setLongitude(128.902587890625).build();
        fBallTestUtil.makeRandomBallSave(centerPosition2, distance, 1000, true);
        //when
        BallFromMapAreaReqDto ballFromMapAreaReqDto = new BallFromMapAreaReqDto(
                37.00255267215955,
                126.18347167968749,
                38.026458711461245,
                127.66937255859375, centerPosition.getLatitude(), centerPosition.getLongitude());

        List<FBall> byBallListUpFromMapArea = fBallQueryRepository.findByBallListUpFromMapArea(ballFromMapAreaReqDto);

        //then
        assertEquals(byBallListUpFromMapArea.size(),1000);

    }


}