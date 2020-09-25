package com.wing.forutona.FTag.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BallIGridMapOfInfluence.Repository.BallIGridMapOfInfluenceDataRepository;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.FBall.Service.BallOfInfluenceCalc;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.FBallTagResDto;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Dto.TagRankingResDto;
import com.wing.forutona.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@Transactional
class FTagServiceImplTest extends BaseTest {

    @Autowired
    FTagService fTagService;

    @Autowired
    FBallTagQueryRepository fBallTagQueryRepository;

    @Autowired
    FBallTagDataRepository fBallTagDataRepository;

    @Autowired
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    BallIGridMapOfInfluenceDataRepository ballIGridMapOfInfluenceDataRepository;

    @Autowired
    BallOfInfluenceCalc ballOfInfluenceCalc;

    @Test
    @DisplayName("검색 범위 탐색 하여 최고 BI의 Ball의 Bi 순으로 정렬 ")
    void getFTagRankingFromBallInfluencePower() throws ParseException {
        //given
        fBallTagDataRepository.deleteAll();
        fBallDataRepository.deleteAll();
        ballIGridMapOfInfluenceDataRepository.deleteAll();
        LatLng setupPoint = LatLng.newBuilder().setLongitude(126.92031158021636).setLatitude(37.5012).build();
        makeRandomBallWithTagSave(setupPoint,100000,200,true);
        int distanceOfBallCountToLimit = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(setupPoint);
        List<FBall> byCriteriaBallFromDistance = fBallQueryRepository.findByCriteriaBallFromDistance(setupPoint, distanceOfBallCountToLimit);


        List<FBall> fBallSortBI = byCriteriaBallFromDistance.stream().map(x -> {
            x.setBI(ballOfInfluenceCalc.calc(x, setupPoint));
            return x;
        }).sorted(Comparator.comparing(FBall::getBI).reversed()).collect(Collectors.toList());

        List<FBalltag> topBITag = fBallTagDataRepository.findByBallUuid(
                fBallSortBI.get(0));

        //when
        List<TagRankingResDto> fTagRankingFromBallInfluencePower = fTagService.getFTagRankingFromBallInfluencePower(setupPoint, 10);

        //then
        assertEquals(topBITag.get(0).getTagItem(),fTagRankingFromBallInfluencePower.get(0).getTagName());
        assertEquals(10,fTagRankingFromBallInfluencePower.size());
    }

    @Test
    @DisplayName("같은 Tag BI 합쳐진것 테스트 ")
    void getFTagRankingFromBallInfluencePowerWithSameTagSumBi() throws ParseException {
        //given
        fBallTagDataRepository.deleteAll();
        fBallDataRepository.deleteAll();
        ballIGridMapOfInfluenceDataRepository.deleteAll();
        LatLng setupPoint = LatLng.newBuilder().setLongitude(126.92031158021636).setLatitude(37.5012).build();
        makeRandomBallWithTagSameSave(setupPoint,100,3,true);
        int distanceOfBallCountToLimit = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(setupPoint);
        List<FBall> byCriteriaBallFromDistance = fBallQueryRepository.findByCriteriaBallFromDistance(setupPoint, distanceOfBallCountToLimit);

        List<FBall> fBallSortBI = byCriteriaBallFromDistance.stream().map(x -> {
            x.setBI(ballOfInfluenceCalc.calc(x, setupPoint));
            return x;
        }).sorted(Comparator.comparing(FBall::getBI).reversed()).collect(Collectors.toList());

        double totalBI = fBallSortBI.stream().mapToDouble(x->x.getBI()).sum();

        //when
        List<TagRankingResDto> fTagRankingFromBallInfluencePower = fTagService.getFTagRankingFromBallInfluencePower(setupPoint, 10);

        //then
        assertEquals(1,fTagRankingFromBallInfluencePower.size());
        assertEquals(Math.round(totalBI*1000)/1000,Math.round(fTagRankingFromBallInfluencePower.get(0).getTagPower()*1000)/1000);
    }


    void makeRandomBallWithTagSave(LatLng findPosition, int distance, int count, boolean isAlive){
        for(int i=0;i<count;i++){
            LatLng randomLocation = GisGeometryUtil.getRandomLocation(findPosition.getLongitude(), findPosition.getLatitude(), distance);
            FBall tempBall = makeFBall(isAlive, randomLocation);
            FBall ballUuid = fBallDataRepository.save(tempBall);
            fBallTagDataRepository.save(FBalltag.builder().ballUuid(ballUuid).tagItem(i+"Tag1").build());
        }
    }

    void makeRandomBallWithTagSameSave(LatLng findPosition, int distance, int count, boolean isAlive){
        for(int i=0;i<count;i++){
            LatLng randomLocation = GisGeometryUtil.getRandomLocation(findPosition.getLongitude(), findPosition.getLatitude(), distance);
            FBall tempBall = makeFBall(isAlive, randomLocation);
            FBall ballUuid = fBallDataRepository.save(tempBall);
            fBallTagDataRepository.save(FBalltag.builder().ballUuid(ballUuid).tagItem("Tag1").build());
        }
    }

    private FBall makeFBall(boolean isAlive, LatLng randomLocation) {
        return FBall.builder()
                .activationTime(isAlive ? LocalDateTime.now().plusSeconds((int) (Math.random() * 100000)) : LocalDateTime.now().minusSeconds((int) (Math.random() * 100000)))
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
                .ballPower(20L)
                .build();
    }

}