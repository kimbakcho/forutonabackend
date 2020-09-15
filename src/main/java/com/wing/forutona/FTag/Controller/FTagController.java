package com.wing.forutona.FTag.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FTag.Dto.FBallTagResDto;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Dto.TagRankingResDto;
import com.wing.forutona.FTag.Service.FTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FTagController {

    final FTagService fTagService;

    @GetMapping(value = "/v1/FTag/RankingFromBallInfluencePower")
    public List<TagRankingResDto> getFTagRankingFromBallInfluencePower(TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto) throws ParseException {
//        return fTagService.getFTagRankingFromBallInfluencePower(tagRankingFromBallInfluencePowerReqDto);
        return null;
    }

    @GetMapping(value = "/v1/FTag/RelationTagRankingFromTagNameOrderByBallPower")
    public List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag) {
        return fTagService.getRelationTagRankingFromTagNameOrderByBallPower(searchTag);
    }

    @GetMapping(value = "/v1/FTag/tagFromBallUuid")
    public List<FBallTagResDto> getTagFromBallUuid(String ballUuid) {
        return fTagService.getTagFromBallUuid(ballUuid);
    }
}
