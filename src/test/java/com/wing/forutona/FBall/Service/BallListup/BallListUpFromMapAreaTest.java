package com.wing.forutona.FBall.Service.BallListup;

import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.FBall.Dto.BallFromMapAreaReqDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class BallListUpFromMapAreaTest {

    @Autowired
    BallListUpFromMapArea  ballListUpFromMapArea;
    @MockBean
    FBallQueryRepository fBallQueryRepository;

    @Test
    void search() throws Exception {
        //given
        BallFromMapAreaReqDto reqDto = new BallFromMapAreaReqDto();
        FSorts fSorts = new FSorts();
        Pageable pageable = PageRequest.of(0, 10);
        //when
        ballListUpFromMapArea.search(reqDto,fSorts,pageable);
        //then
        verify(fBallQueryRepository).getBallListUpFromMapArea(reqDto,fSorts,pageable);

    }
}