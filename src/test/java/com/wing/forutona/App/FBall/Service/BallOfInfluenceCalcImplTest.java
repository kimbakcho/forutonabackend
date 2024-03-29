package com.wing.forutona.App.FBall.Service;

import com.google.type.LatLng;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.BallIGridMapOfInfluence;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.LatitudeLongitude;
import com.wing.forutona.App.BallIGridMapOfInfluence.Repository.BallIGridMapOfInfluenceDataRepository;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.FBallState;
import com.wing.forutona.App.FBall.Domain.FBallType;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class BallOfInfluenceCalcImplTest extends BaseTest {

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    BallIGridMapOfInfluenceDataRepository ballIGridMapOfInfluenceDataRepository;

    @Autowired
    BallOfInfluenceCalc ballOfInfluenceCalc;

    @Test
    @DisplayName("BI 계산 BallIGridMapOfInfluence 있을때 테스트")
    void calcHaveBallIGridMapOfInfluence() {
        //given
        ballIGridMapOfInfluenceDataRepository.deleteAll();
        ballIGridMapOfInfluenceDataRepository.flush();
        String ballUuid = UUID.randomUUID().toString();

        Double ballLongitude = 126.8976;
        Double ballLatitude = 37.5012;

        FBall ball = fBallDataRepository.save(FBall.builder()
                .pointReward(0)
                .placeAddress("TEST")
                .makeTime(LocalDateTime.now().minusHours(20))
                .makeExp(300)
                .longitude(ballLongitude)
                .latitude(ballLatitude)
                .influenceReward(0)
                .description("{}")
                .ballUuid(ballUuid)
                .ballType(FBallType.IssueBall)
                .ballState(FBallState.Play)
                .ballName("TEST Ball")
                .ballHits(0)
                .activationTime(LocalDateTime.now().plusDays(3))
                .uid(testUser)
                .ballPower(40)
                .build());

        double roundBallLongitude = Math.round(ballLongitude * 100.0) / 100.0;
        double roundBallLatitude = Math.round(ballLatitude * 100.0) / 100.0;
        LatitudeLongitude latitudeLongitude = new LatitudeLongitude(roundBallLatitude,roundBallLongitude);

        ballIGridMapOfInfluenceDataRepository.saveAndFlush(BallIGridMapOfInfluence.builder()
                .latitudeLongitude(latitudeLongitude)
                .HGABP(0.02)
                .MGABP(1)
                .MGAU(1)
                .MGBC(1)
                .SummaryUpdateTime(LocalDateTime.now())
                .build());

        LatLng userPosition = GisGeometryUtil.getRandomExactLocation(ballLongitude,ballLatitude,2000);

        //when
        double resultBI = ballOfInfluenceCalc.calc(ball, userPosition);
        //then
        assertEquals(0.02,Math.round(resultBI*100.0)/100.0);
    }

    @Test
    @DisplayName("BI 계산 BallIGridMapOfInfluence 없을때 테스트")
    void calcDontHaveBallIGridMapOfInfluence() {
        //given
        ballIGridMapOfInfluenceDataRepository.deleteAll();
        ballIGridMapOfInfluenceDataRepository.flush();
        String ballUuid = UUID.randomUUID().toString();

        Double ballLongitude = 126.8976;
        Double ballLatitude = 37.5012;

        FBall ball = fBallDataRepository.save(FBall.builder()
                .pointReward(0)
                .placeAddress("TEST")
                .makeTime(LocalDateTime.now().minusHours(20))
                .makeExp(300)
                .longitude(ballLongitude)
                .latitude(ballLatitude)
                .influenceReward(0)
                .description("{}")
                .ballUuid(ballUuid)
                .ballType(FBallType.IssueBall)
                .ballState(FBallState.Play)
                .ballName("TEST Ball")
                .ballHits(0)
                .activationTime(LocalDateTime.now().plusDays(3))
                .uid(testUser)
                .ballPower(40)
                .build());


        LatLng userPosition = GisGeometryUtil.getRandomExactLocation(ballLongitude,ballLatitude,2000);

        //when
        double resultBI = ballOfInfluenceCalc.calc(ball, userPosition);
        //then
        assertEquals(0.009,Math.round(resultBI*1000.0)/1000.0);
    }

}