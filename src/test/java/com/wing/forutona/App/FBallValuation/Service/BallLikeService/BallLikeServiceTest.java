package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.BaseTest;
import com.wing.forutona.App.Contributors.Domain.Contributors;
import com.wing.forutona.App.FBall.Domain.*;
import com.wing.forutona.App.FBallValuation.Domain.LikeActionType;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteReqDto;
import com.wing.forutona.App.Contributors.Repository.ContributorsDataRepository;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
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
                .ballPower(0)
                .makeTime(LocalDateTime.now())
                .build();
        fBallDataRepository.save(testBall);
    }

    @Test
    void BallLikeServiceExecuteTest() throws Exception {
        //given
        FBall fBall1 = fBallDataRepository.findById("TESTBBallUuid").get();
        FBallVoteReqDto reqDto = new FBallVoteReqDto();

        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(0);
        reqDto.setLikePoint(5);
//        reqDto.setLikeActionType(LikeActionType.LIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        //when
        likeService.execute(reqDto,testUser.getUid());

        //then
//        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();
//        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, testUser).get();
//        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(testUser, fBall);
//        assertEquals(5,fBall.getBallLikes());
//        assertEquals(0,fBall.getBallDisLikes());
//        assertEquals(5,fBallValuation.getPoint());
//        assertEquals(5,fBall.getBallPower());
//        assertTrue(contributorsOptional.isPresent());
    }

    @Test
    void BallDisLikeServiceExecuteTest() throws Exception {
        //given

        FBallVoteReqDto reqDto = new FBallVoteReqDto();

        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(5);
        reqDto.setLikePoint(0);
//        reqDto.setLikeActionType(LikeActionType.DISLIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        //when
        likeService.execute(reqDto,testUser.getUid());

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();

//        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, testUser).get();
//        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(testUser, fBall);
//        assertEquals(0,fBall.getBallLikes());
//        assertEquals(5,fBall.getBallDisLikes());
//        assertEquals(-5,fBallValuation.getPoint());
//        assertEquals(-5,fBall.getBallPower());
//        assertTrue(contributorsOptional.isPresent());
    }


    @Test
    void BallLikeCancelServiceExecuteTest() throws Exception {
        //given
        FBallVoteReqDto reqDto = new FBallVoteReqDto();
        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(0);
        reqDto.setLikePoint(5);
//        reqDto.setLikeActionType(LikeActionType.LIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        likeService.execute(reqDto,testUser.getUid());

        FBallVoteReqDto cancelReqDto = new FBallVoteReqDto();
        cancelReqDto.setBallUuid("TESTBBallUuid");
        cancelReqDto.setValueUuid("TESTValueUuid");
        cancelReqDto.setDisLikePoint(0);
        cancelReqDto.setLikePoint(5);
        cancelReqDto.setLikeActionType(LikeActionType.CANCEL);

        BallLikeService cancelService = ballLIkeServiceFactory.create(cancelReqDto.getLikeActionType());
        //when
        cancelService.execute(cancelReqDto,testUser.getUid());

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();

//        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, testUser).get();
//        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(testUser, fBall);
//        assertEquals(0,fBall.getBallLikes());
//        assertEquals(0,fBall.getBallDisLikes());
//        assertEquals(0,fBallValuation.getPoint());
//        assertEquals(0,fBall.getBallPower());
//        assertTrue(contributorsOptional.isEmpty());
    }
}