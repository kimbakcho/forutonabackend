package com.wing.forutona.FBallValuation.Service.BallLikeService;

import com.wing.forutona.BaseTest;
import com.wing.forutona.Contributors.Domain.Contributors;
import com.wing.forutona.FBall.Domain.*;
import com.wing.forutona.FBallValuation.Domain.LikeActionType;
import com.wing.forutona.FBallValuation.Dto.FBallLikeReqDto;
import com.wing.forutona.Contributors.Repository.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class BallLikeServiceTest extends BaseTest {

    @Autowired
    BallLIkeServiceFactory ballLIkeServiceFactory;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;


    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallValuationDataRepository fBallValuationDataRepository;

    @Autowired
    ContributorsDataRepository contributorsDataRepository;

    FBall testBall;

    @BeforeEach
    void beforeEach(){
        testBall = FBall.builder()
                .ballUuid("TESTBBallUuid")
                .ballName("TESTBall")
                .ballState(FBallState.Play)
                .ballType(FBallType.IssueBall)
                .activationTime(LocalDateTime.now().plusDays(7))
                .description("{}")
                .latitude(37.4402052)
                .longitude(126.79369789999998)
                .uid(testUser)
                .makeTime(LocalDateTime.now())
                .build();
        fBallDataRepository.save(testBall);
    }

    @Test
    void BallLikeServiceExecuteTest() throws Exception {
        //given
        FBall fBall1 = fBallDataRepository.findById("TESTBBallUuid").get();
        FBallLikeReqDto reqDto = new FBallLikeReqDto();

        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(0L);
        reqDto.setLikePoint(5L);
        reqDto.setLikeActionType(LikeActionType.LIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        //when
        likeService.execute(reqDto,testUser.getUid());

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, testUser).get();
        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(testUser, fBall);
        assertEquals(5L,fBall.getBallLikes());
        assertEquals(0L,fBall.getBallDisLikes());
        assertEquals(5L,fBallValuation.getPoint());
        assertEquals(5L,fBall.getBallPower());
        assertTrue(contributorsOptional.isPresent());
    }

    @Test
    void BallDisLikeServiceExecuteTest() throws Exception {
        //given

        FBallLikeReqDto reqDto = new FBallLikeReqDto();

        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(5L);
        reqDto.setLikePoint(0L);
        reqDto.setLikeActionType(LikeActionType.DISLIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        //when
        likeService.execute(reqDto,testUser.getUid());

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();

        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, testUser).get();
        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(testUser, fBall);
        assertEquals(0,fBall.getBallLikes());
        assertEquals(5L,fBall.getBallDisLikes());
        assertEquals(-5L,fBallValuation.getPoint());
        assertEquals(-5L,fBall.getBallPower());
        assertTrue(contributorsOptional.isPresent());
    }


    @Test
    void BallLikeCancelServiceExecuteTest() throws Exception {
        //given
        FBallLikeReqDto reqDto = new FBallLikeReqDto();
        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(0L);
        reqDto.setLikePoint(5L);
        reqDto.setLikeActionType(LikeActionType.LIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        likeService.execute(reqDto,testUser.getUid());

        FBallLikeReqDto cancelReqDto = new FBallLikeReqDto();
        cancelReqDto.setBallUuid("TESTBBallUuid");
        cancelReqDto.setValueUuid("TESTValueUuid");
        cancelReqDto.setDisLikePoint(0L);
        cancelReqDto.setLikePoint(5L);
        cancelReqDto.setLikeActionType(LikeActionType.CANCEL);

        BallLikeService cancelService = ballLIkeServiceFactory.create(cancelReqDto.getLikeActionType());
        //when
        cancelService.execute(cancelReqDto,testUser.getUid());

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();

        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, testUser).get();
        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(testUser, fBall);
        assertEquals(0L,fBall.getBallLikes());
        assertEquals(0L,fBall.getBallDisLikes());
        assertEquals(0L,fBallValuation.getPoint());
        assertEquals(0L,fBall.getBallPower());
        assertTrue(contributorsOptional.isEmpty());
    }
}