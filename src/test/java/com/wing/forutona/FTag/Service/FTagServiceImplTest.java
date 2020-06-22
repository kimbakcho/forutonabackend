package com.wing.forutona.FTag.Service;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import com.wing.forutona.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

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
        //given
        given(distanceOfBallCountToLimitService.distanceOfBallCountToLimit(anyDouble(),anyDouble(),anyInt())).willReturn(1000);
        given(fBallTagQueryRepository.getFindTagRankingInDistanceOfInfluencePower(any(),any(),anyInt())).willReturn(new TagRankingWrapdto());
        TagRankingFromBallInfluencePowerReqDto reqDto = new TagRankingFromBallInfluencePowerReqDto();
        reqDto.setLatitude(127.0);
        reqDto.setLongitude(30);
        reqDto.setLimit(1000);
        //when
        fTagService.getFTagRankingFromBallInfluencePower(reqDto);
        //then
        InOrder inOrder = inOrder(distanceOfBallCountToLimitService,fBallTagQueryRepository);

        then(distanceOfBallCountToLimitService).should(inOrder).distanceOfBallCountToLimit(anyDouble(),anyDouble(),anyInt());
        then(fBallTagQueryRepository).should(inOrder).getFindTagRankingInDistanceOfInfluencePower(any(),any(),anyInt());

    }


}