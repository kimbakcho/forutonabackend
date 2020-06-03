package com.wing.forutona;

import com.querydsl.core.types.Order;
import com.wing.forutona.CustomUtil.MultiSort;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Dto.UserToMakerBallReqDto;
import com.wing.forutona.FBall.Dto.UserToMakerBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        MultiSorts sorts = new MultiSorts();
        List<MultiSort> sort = new ArrayList<>();
        sort.add(new MultiSort("Alive", Order.DESC));
        sort.add(new MultiSort("startTime", Order.DESC));
        sorts.setSorts(sort);

        List<UserToPlayBallResDto> fBallPlayerByPlayer = fBallPlayerQueryRepository.getUserToPlayBallList(dto,sorts, pageable);


        for (UserToPlayBallResDto result : fBallPlayerByPlayer) {
            System.out.println(result.getBallUuid());
        }

    }

    @Test
    public  void getUserToMakerBalls(){
        UserToMakerBallReqDto dto = new UserToMakerBallReqDto();
        dto.setMakerUid("h2q2jl3nRPXZ8809Uvi9KdzSss83");

        Pageable pageable = PageRequest.of(0, 20);
        MultiSorts sorts = new MultiSorts();
        List<MultiSort> sort = new ArrayList<>();
        sort.add(new MultiSort("Alive", Order.DESC));
        sort.add(new MultiSort("makeTime", Order.DESC));
        sorts.setSorts(sort);

        List<UserToMakerBallResDto> userToMakerBalls = fBallQueryRepository.getUserToMakerBalls(dto, sorts,pageable);
        for (UserToMakerBallResDto userToMakerBall : userToMakerBalls) {
            System.out.println(userToMakerBall.getBallName());
            System.out.println(userToMakerBall.getMakeTime());
            System.out.println("++++++++++++++++++++++++++++++++++");
        }

    }

}
