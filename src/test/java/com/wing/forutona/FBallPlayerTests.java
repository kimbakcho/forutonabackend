package com.wing.forutona;

import com.querydsl.core.QueryResults;
import com.wing.forutona.FBall.Dto.UserToMakerBallReqDto;
import com.wing.forutona.FBall.Dto.UserToMakerBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerDataRepositroy;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
import com.wing.forutona.FBall.Service.FBallPlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles(profiles = "local")
@Transactional
public class FBallPlayerTests {
    @Autowired
    FBallPlayerQueryRepository fBallPlayerQueryRepository;

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Test
    public void UserToPlayBallListTest(){
        UserToPlayBallReqDto dto = new UserToPlayBallReqDto();
        dto.setPlayerUid("u2eLk6WRW0YUd8Hc1XDSfSHlrxY2");

        Pageable pageable = PageRequest.of(0, 20, Sort.by("Alive").descending().and(Sort.by("startTime").descending()));

        List<UserToPlayBallResDto> fBallPlayerByPlayer = fBallPlayerQueryRepository.findFBallPlayerByPlayer(dto, pageable);


        for (UserToPlayBallResDto result : fBallPlayerByPlayer) {
            System.out.println(result.getFBalluuid());
        }

    }

    @Test
    public  void getUserToMakerBalls(){
        UserToMakerBallReqDto dto = new UserToMakerBallReqDto();
        dto.setMakerUid("h2q2jl3nRPXZ8809Uvi9KdzSss83");

        Pageable pageable = PageRequest.of(0, 20, Sort.by("Alive").descending().and(Sort.by("makeTime").descending()));

        List<UserToMakerBallResDto> userToMakerBalls = fBallQueryRepository.getUserToMakerBalls(dto, pageable);
        for (UserToMakerBallResDto userToMakerBall : userToMakerBalls) {
            System.out.println(userToMakerBall.getBallName());
            System.out.println(userToMakerBall.getMakeTime());
            System.out.println("++++++++++++++++++++++++++++++++++");
        }

    }

}
