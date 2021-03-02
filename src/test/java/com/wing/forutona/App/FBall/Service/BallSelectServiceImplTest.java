package com.wing.forutona.App.FBall.Service;

import com.wing.forutona.BaseTest;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.FBallState;
import com.wing.forutona.App.FBall.Domain.FBallType;
import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
class BallSelectServiceImplTest extends BaseTest {

    @MockBean
    FBallDataRepository fBallDataRepository;

    @Autowired
    BallSelectService ballSelectService;

    @Test
    void selectBall() {
        //given
        String testBallUuid = "testBallUuid";
        FBall fBall = FBall.builder()
                .ballUuid(testBallUuid)
                .ballName("TESTBall")
                .ballState(FBallState.Play)
                .ballType(FBallType.IssueBall)
                .activationTime(LocalDateTime.now().plusDays(7))
                .description("{}")
                .latitude(37.4402052)
                .longitude(126.79369789999998)
                .uid(testUser)
                .makeTime(LocalDateTime.now())
                .ballPower(0)
                .build();
        Optional<FBall> optionalFBall = Optional.of(fBall);
        when(fBallDataRepository.findById(testBallUuid)).thenReturn(optionalFBall);
        //when
        FBallResDto fBallResDto = ballSelectService.selectBall(testBallUuid);
        //then
        assertEquals(testBallUuid,fBallResDto.getBallUuid());
    }
}