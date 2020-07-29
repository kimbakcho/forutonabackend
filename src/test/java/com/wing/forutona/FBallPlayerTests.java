package com.wing.forutona;

import com.querydsl.core.types.Order;
import com.wing.forutona.CustomUtil.FSort;
import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        FSorts sorts = new FSorts();
        List<FSort> sort = new ArrayList<>();
        sort.add(new FSort("Alive", Order.DESC));
        sort.add(new FSort("startTime", Order.DESC));
        sorts.setSorts(sort);

        List<UserToPlayBallResDto> fBallPlayerByPlayer = fBallPlayerQueryRepository.getUserToPlayBallList(dto,sorts, pageable);


        for (UserToPlayBallResDto result : fBallPlayerByPlayer) {
            System.out.println(result.getBallUuid());
        }

    }

    @Test
    public  void getUserToMakerBalls(){
        Pageable pageable = PageRequest.of(0, 20);
        FSorts sorts = new FSorts();
        List<FSort> sort = new ArrayList<>();
        sort.add(new FSort("Alive", Order.DESC));
        sort.add(new FSort("makeTime", Order.DESC));
        sorts.setSorts(sort);

        Page<FBallResDto> userToMakerBalls = fBallQueryRepository.getUserToMakerBalls("h2q2jl3nRPXZ8809Uvi9KdzSss83",pageable);
        for (FBallResDto fBallResDto : userToMakerBalls.getContent()) {
            System.out.println(fBallResDto.getBallName());
            System.out.println(fBallResDto.getMakeTime());
            System.out.println("++++++++++++++++++++++++++++++++++");
        }

    }

}
