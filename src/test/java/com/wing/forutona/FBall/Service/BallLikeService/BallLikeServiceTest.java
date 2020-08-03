package com.wing.forutona.FBall.Service.BallLikeService;

import com.google.firebase.auth.UserInfo;
import com.wing.forutona.FBall.Domain.*;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoSimpleDataRepository;
import org.hibernate.annotations.ColumnDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BallLikeServiceTest {

    @Autowired
    BallLIkeServiceFactory ballLIkeServiceFactory;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository;


    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallValuationDataRepository fBallValuationDataRepository;

    @Autowired
    ContributorsDataRepository contributorsDataRepository;

    FUserInfoSimple testUser;

    FBall testBall;

    @ColumnDefault("37.4402052")
    private Double latitude;
    @ColumnDefault("126.79369789999998")
    private Double longitude;

    @BeforeEach
    void beforeEach(){
        FUserInfo testSaveUsers = FUserInfo.builder().uid("TESTUserUid").nickName("TESTNickName").fCMtoken("TEST")
                .latitude(37.4402052).longitude(126.79369789999998).build();

        fUserInfoDataRepository.save(testSaveUsers);
        this.testUser = FUserInfoSimple.builder().uid("TESTUserUid").nickName("TESTNickName").build();
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

        FBallLikeReqDto reqDto = new FBallLikeReqDto();

        reqDto.setBallUuid("TESTBBallUuid");
        reqDto.setValueUuid("TESTValueUuid");
        reqDto.setDisLikePoint(0L);
        reqDto.setLikePoint(5L);
        reqDto.setLikeActionType(LikeActionType.LIKE);

        BallLikeService likeService = ballLIkeServiceFactory.create(reqDto.getLikeActionType());

        //when
        likeService.execute(reqDto,"TESTUserUid");

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();
        FUserInfoSimple fUserInfo = fUserInfoSimpleDataRepository.findById("TESTUserUid").get();
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfo).get();
        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo, fBall);
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
        likeService.execute(reqDto,"TESTUserUid");

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();
        FUserInfoSimple fUserInfo = fUserInfoSimpleDataRepository.findById("TESTUserUid").get();
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfo).get();
        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo, fBall);
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

        likeService.execute(reqDto,"TESTUserUid");

        FBallLikeReqDto cancelReqDto = new FBallLikeReqDto();
        cancelReqDto.setBallUuid("TESTBBallUuid");
        cancelReqDto.setValueUuid("TESTValueUuid");
        cancelReqDto.setDisLikePoint(0L);
        cancelReqDto.setLikePoint(5L);
        cancelReqDto.setLikeActionType(LikeActionType.CANCEL);

        BallLikeService cancelService = ballLIkeServiceFactory.create(cancelReqDto.getLikeActionType());
        //when
        cancelService.execute(cancelReqDto,"TESTUserUid");

        //then
        FBall fBall = fBallDataRepository.findById("TESTBBallUuid").get();
        FUserInfoSimple fUserInfo = fUserInfoSimpleDataRepository.findById("TESTUserUid").get();
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfo).get();
        Optional<Contributors> contributorsOptional = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo, fBall);
        assertEquals(0L,fBall.getBallLikes());
        assertEquals(0L,fBall.getBallDisLikes());
        assertEquals(0L,fBallValuation.getPoint());
        assertEquals(0L,fBall.getBallPower());
        assertTrue(contributorsOptional.isEmpty());
    }
}