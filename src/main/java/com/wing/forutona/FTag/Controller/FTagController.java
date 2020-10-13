package com.wing.forutona.FTag.Controller;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.FTag.Dto.*;
import com.wing.forutona.FTag.Service.FTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FTagController {

    final FTagService fTagService;

    @GetMapping(value = "/v1/FTag/RankingFromBallInfluencePower")
    public List<TagRankingResDto> getFTagRankingFromBallInfluencePower(TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto) throws ParseException {
        LatLng findPosition = LatLng.newBuilder()
                .setLongitude(tagRankingFromBallInfluencePowerReqDto.getMapCenterLongitude())
                .setLatitude(tagRankingFromBallInfluencePowerReqDto.getMapCenterLatitude())
                .build();
        return fTagService.getFTagRankingFromBallInfluencePower(findPosition, 10);
    }

    @GetMapping(value = "/v1/FTag/RelationTagRankingFromTagNameOrderByBallPower")
    public List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag) {
        return fTagService.getRelationTagRankingFromTagNameOrderByBallPower(searchTag);
    }

    @GetMapping(value = "/v1/FTag/TagRankingFromTextOrderBySumBI")
    public List<TagRankingResDto> getTagRankingFromTextOrderBySumBI(TagRankingFromTextReqDto tagRankingFromTextReqDto) throws ParseException {
        return fTagService.getTagRankingFromTextOrderBySumBI(tagRankingFromTextReqDto);
    }

    @GetMapping(value = "/v1/FTag/TagItem")
    public Page<FBallTagResDto> getTagItem(TextMatchTagBallReqDto reqDto, Pageable pageable) throws ParseException {
        return fTagService.getTagItem(reqDto, pageable);
    }

    @GetMapping(value = "/v1/FTag/tagFromBallUuid")
    public List<FBallTagResDto> getTagFromBallUuid(String ballUuid) {
        return fTagService.getTagFromBallUuid(ballUuid);
    }
}
