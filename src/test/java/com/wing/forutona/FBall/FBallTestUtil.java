package com.wing.forutona.FBall;

import com.google.type.LatLng;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

public class FBallTestUtil  {


    FBallDataRepository fBallDataRepository;
    FUserInfo testUser;

    public FBallTestUtil(FBallDataRepository fBallDataRepository, FUserInfo fUserInfo){
        this.fBallDataRepository = fBallDataRepository;
        this.testUser = fUserInfo;
    }


    public void makeRandomBallSave(LatLng findPosition, int distance, int count, boolean isAlive){
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
