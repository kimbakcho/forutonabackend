package com.wing.forutona.FTag.Service;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@Transactional
class FTagServiceImplTest extends BaseTest {

    @Autowired
    FTagService fTagService;

    @MockBean
    FBallTagQueryRepository fBallTagQueryRepository;

    @MockBean
    FBallTagDataRepository fBallTagDataRepository;

    @MockBean
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Test
    @DisplayName("검색 범위 탐색 하여 Repository Call")
    void getFTagRankingFromBallInfluencePower() throws ParseException {
        //TODO 다시 작성 필요

    }


}