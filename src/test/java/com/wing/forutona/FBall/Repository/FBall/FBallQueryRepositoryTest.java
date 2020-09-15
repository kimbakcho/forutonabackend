package com.wing.forutona.FBall.Repository.FBall;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
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

    @Test
    @DisplayName("최고 강한 영향력 볼 만들어 리턴 받기")
    void getBallListUpFromBallInfluencePower() throws ParseException {
        //given
        List<FBall> fBalls = updateAllAliveBall();
        FBall choiceFBall = randomChoiceStringInfluencePowerBall(fBalls);
        makeStrongInfluencePowerBall(choiceFBall);

        Pageable pageable = PageRequest.of(0, 999);
        //when
        Page<FBallResDto> ballListUpFromBallInfluencePower = fBallQueryRepository.getBallListUpFromBallInfluencePower(
                GisGeometryUtil.createPoint(37.50198846403655, 126.89106021076441),
                GisGeometryUtil.createSquareFromCenterPosition(LatLng.newBuilder()
                        .setLongitude(126.89106021076441)
                        .setLatitude(37.50198846403655)
                        .build()
                        , 100000.0),
                pageable
        );

        then(ballListUpFromBallInfluencePower.getContent().size()).isGreaterThan(0);
        then(ballListUpFromBallInfluencePower.getContent().get(0).getBallUuid()).isEqualTo(choiceFBall.getBallUuid());
    }

    private FBall randomChoiceStringInfluencePowerBall(List<FBall> fBalls) {
        double dValue = Math.random();
        int iValue = (int) (dValue * fBalls.size());
        return fBalls.get(iValue);
    }

    private List<FBall> updateAllAliveBall() {
        List<FBall> fBalls = fBallDataRepository.findAll();
        for (FBall ball : fBalls) {
            ball.setActivationTime(LocalDateTime.now().plusDays(1));
        }
        return fBalls;
    }

    private void makeStrongInfluencePowerBall(FBall fBall) {
        fBall.setBallPower(100L);
        fBall.setPlacePoint(126.89706021076441, 37.50298846403655);
    }

    @Test()
    @DisplayName("기준 조건의 Ball 갯수 확인 현재 기준은 살아 있는 볼")
    void criteriaBallDistanceRangeCount() throws ParseException {
        //given
        fBallDataRepository.deleteAll();
        LatLng centerPosition = LatLng.newBuilder().setLatitude(37.5088099).setLongitude(126.8912061).build();
        int km = 1000;
        int distance = 10 * km;
        makeRandomBallSave(centerPosition,distance,100,true);
        makeRandomBallSave(centerPosition,distance,20,false);
        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(36.5580266).setLongitude(127.5069707).build();
        makeRandomBallSave(centerPosition2,distance,100,true);

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
        makeRandomBallSave(centerPosition,distance,1000,true);

        LatLng centerPosition2 = LatLng.newBuilder().setLatitude(35.429344044107154).setLongitude(128.6334228515625).build();
        makeRandomBallSave(centerPosition2,distance,500,true);
        //when
        long findBallCount = fBallQueryRepository.findByCountIsCriteriaBallFromDistance(centerPosition, distance);
        //then
        assertEquals(1000,findBallCount);
    }


    void makeRandomBallSave(LatLng findPosition,int distance,int count,boolean isAlive){
        for(int i=0;i<count;i++){
            LatLng randomLocation = GisGeometryUtil.getRandomLocation(findPosition.getLongitude(), findPosition.getLatitude(), distance);
            FBall tempBall = FBall.builder()
                    .activationTime(isAlive? LocalDateTime.now().plusSeconds((int) (Math.random() * 100000)):  LocalDateTime.now().minusSeconds((int) (Math.random() * 100000)))
                    .ballHits(0)
                    .ballName("BallName" + (int) (Math.random() * 100000))
                    .ballState(FBallState.Play)
                    .ballType(FBallType.IssueBall)
                    .ballUuid(UUID.randomUUID().toString())
                    .description("{}")
                    .influenceReward(0)
                    .latitude(randomLocation.getLatitude())
                    .longitude(randomLocation.getLongitude())
                    .makeExp(300)
                    .makeTime(LocalDateTime.now().minusSeconds((int) (Math.random() * 100000)))
                    .placeAddress("address" + (int) (Math.random() * 100000))
                    .uid(testUser)
                    .pointReward(0)
                    .ballPower(0L)
                    .build();
            fBallDataRepository.save(tempBall);
        }
    }



}