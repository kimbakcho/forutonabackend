package com.wing.forutona.App.FTag.Repository;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.App.FTag.Dto.FBallTagResDto;
import com.wing.forutona.App.FTag.Dto.TextMatchTagBallReqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;

@Transactional
class FBallTagQueryRepositoryTest extends BaseTest {

    @Autowired
    FBallTagQueryRepository fBallTagQueryRepository;

    @Test
    void findByTextMatchTagBall() throws ParseException {
        //given
        TextMatchTagBallReqDto textMatchTagBallReqDto = new TextMatchTagBallReqDto();
        textMatchTagBallReqDto.setMapCenterLatitude(35.285984736065764);
        textMatchTagBallReqDto.setMapCenterLongitude(128.902587890625);
        textMatchTagBallReqDto.setSearchText("test");
        //when
        Page<FBallTagResDto> ballPower = fBallTagQueryRepository.findByTagItem(textMatchTagBallReqDto,
                PageRequest.of(0, 40, Sort.Direction.DESC, "ballPower"));

        Page<FBallTagResDto> ballHits = fBallTagQueryRepository.findByTagItem(textMatchTagBallReqDto,
                PageRequest.of(0, 40, Sort.Direction.DESC, "ballHits"));

        Page<FBallTagResDto> makeTimes = fBallTagQueryRepository.findByTagItem(textMatchTagBallReqDto,
                PageRequest.of(0, 40, Sort.Direction.DESC, "makeTime"));

        Page<FBallTagResDto> distance = fBallTagQueryRepository.findByTagItem(textMatchTagBallReqDto,
                PageRequest.of(0, 40, Sort.Direction.DESC, "distance"));
        //then
        System.out.println(ballPower.getContent().size());

    }
}