package com.wing.forutona.App.FTag.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.App.BallIGridMapOfInfluence.Repository.BallIGridMapOfInfluenceDataRepository;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.FBallState;
import com.wing.forutona.App.FBall.Domain.FBallType;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.App.FBall.Service.BallOfInfluenceCalc;
import com.wing.forutona.App.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.App.FTag.Domain.FBalltag;
import com.wing.forutona.App.FTag.Dto.TagRankingResDto;
import com.wing.forutona.App.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.App.FTag.Repository.FBallTagQueryRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        List<FBalltag> topBITag = fBallTagDataRepository.findByBallUuidOrderByTagIndexAsc(
                fBallSortBI.get(0));

        //when
        List<TagRankingResDto> fTagRankingFromBallInfluencePower = fTagService.getFTagRankingFromBallInfluencePower(setupPoint, 10);

        //then
        assertEquals(topBITag.get(0).getTagItem(),fTagRankingFromBallInfluencePower.get(0).getTagName());
        assertEquals(10,fTagRankingFromBallInfluencePower.size());
    }

    @Test
    @DisplayName("같은 Tag BI 합쳐진것 테스트")
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

    @Test
    @Disabled
    @DisplayName("같은 Tag BI 합쳐진것 테스트")
    void getTagRankingFromTextOrderBySumBI() throws ParseException {
        //given
        LatLng setupPoint = LatLng.newBuilder().setLongitude(126.92031158021636).setLatitude(37.5012).build();

        //FULLTEXT 경우 트랜젝션안에서 값을 넣어서 해봤지만 match 에서 반영이 안되고 DB에 있는 기존값 가지고만 테스트가 가능함.
//        makeRandomBallWithTag(setupPoint,100,true,"영화");
//        makeRandomBallWithTag(setupPoint,100,true,"영화");
//        makeRandomBallWithTag(setupPoint,100,true,"영화");
//        makeRandomBallWithTag(setupPoint,100,true,"영화1");
//        makeRandomBallWithTag(setupPoint,100,true,"1영화1");
//        makeRandomBallWithTag(setupPoint,100,true,"1영화1");
//        makeRandomBallWithTag(setupPoint,100,true,"1영화1");
//        makeRandomBallWithTag(setupPoint,100,true,"a영화");
//        makeRandomBallWithTag(setupPoint,100,true,"영화 추천");
//        makeRandomBallWithTag(setupPoint,100,true,"영화 추천aa");
//        makeRandomBallWithTag(setupPoint,100,true,"aa영화 추천aa");
//        makeRandomBallWithTag(setupPoint,100,true,"aa 영화 추천");
//        makeRandomBallWithTag(setupPoint,100,true,"aa 영화 배우");
//        makeRandomBallWithTag(setupPoint,100,true,"명대사");
//        makeRandomBallWithTag(setupPoint,100,true,"명대사");
//        makeRandomBallWithTag(setupPoint,100,true,"명대사");
//        makeRandomBallWithTag(setupPoint,100,true,"영화명장면");
//        makeRandomBallWithTag(setupPoint,100,true,"영화관");

        //when
        List<FBalltag> tags = fBallTagQueryRepository.findByTextMatchTags("TEST", setupPoint);
        //then
        System.out.println(tags.size());


    }


    void makeRandomBallWithTagSave(LatLng findPosition, int distance, int count, boolean isAlive){
        for(int i=0;i<count;i++){
            makeRandomBallWithTag(findPosition, distance, isAlive, i + "Tag1");
        }
    }

    void makeRandomBallWithTag(LatLng findPosition, int distance, boolean isAlive, String tageName) {
        LatLng randomLocation = GisGeometryUtil.getRandomLocation(findPosition.getLongitude(), findPosition.getLatitude(), distance);
        FBall tempBall = makeFBall(isAlive, randomLocation);
        FBall ballUuid = fBallDataRepository.save(tempBall);
        fBallTagDataRepository.save(FBalltag.builder().ballUuid(ballUuid).tagItem(tageName).build());
    }

    void makeRandomBallWithTagSameSave(LatLng findPosition, int distance, int count, boolean isAlive){
        for(int i=0;i<count;i++){
            makeRandomBallWithTag(findPosition, distance, isAlive, "Tag1");
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
                .ballPower(20)
                .build();
    }

}