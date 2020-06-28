package com.wing.forutona.FBall.Service;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.MapFindScopeStepRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class DistanceOfBallCountToLimitServiceImplTest extends BaseTest {

    @Autowired
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @MockBean
    MapFindScopeStepRepository mapFindScopeStepRepository;

    @MockBean
    FBallQueryRepository fBallQueryRepository;

    @Test
    @DisplayName("Limit 까지 볼 검색시 해당 거리 리턴")
    void distanceOfBallCountToLimit() throws ParseException {
        //given
        List<FMapFindScopeStep> scopeMeter = new ArrayList<FMapFindScopeStep>();
        scopeMeter.add(makeFindStep(0, 100));
        scopeMeter.add(makeFindStep(1, 200));
        scopeMeter.add(makeFindStep(2, 300));
        scopeMeter.add(makeFindStep(3, 400));
        scopeMeter.add(makeFindStep(4, 500));
        scopeMeter.add(makeFindStep(5, 600));
        scopeMeter.add(makeFindStep(6, 700));
        scopeMeter.add(makeFindStep(7, 800));
        scopeMeter.add(makeFindStep(8, 900));
        scopeMeter.add(makeFindStep(9, 1000));
        given(mapFindScopeStepRepository.findAll(Sort.by("scopeMeter").ascending())).willReturn(scopeMeter);
        given(fBallQueryRepository.getFindBallCountInDistance(any()))
                .willReturn(1L).willReturn(10L).willReturn(100L).willReturn(1000L).willReturn(1200L);
        //when
        int result = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(127.0, 31.0, 1000);
        //then
        assertEquals(result,500);

    }

    private FMapFindScopeStep makeFindStep(int idx, int distance) {
        FMapFindScopeStep Step =  FMapFindScopeStep.builder().idx(idx).scopeMeter(distance).build();

        return Step;
    }
}