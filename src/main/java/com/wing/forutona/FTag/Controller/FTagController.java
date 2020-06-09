package com.wing.forutona.FTag.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.FireBaseAdmin;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FTag.Dto.RelationTagRankingFromTagNameReqDto;
import com.wing.forutona.FTag.Dto.TagFromBallReqDto;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpFromTagReqDto;
import com.wing.forutona.FTag.Service.FTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class FTagController {

    @Autowired
    FTagService fTagService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/RankingFromBallInfluencePower")
    public ResponseBodyEmitter getFTagRankingFromBallInfluencePower(TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fTagService.getFTagRankingFromBallInfluencePower(emitter, tagRankingFromBallInfluencePowerReqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/RelationTagRankingFromTagNameOrderByBallPower")
    public ResponseBodyEmitter getRelationTagRankingFromTagNameOrderByBallPower(RelationTagRankingFromTagNameReqDto reqDto) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fTagService.getRelationTagRankingFromTagNameOrderByBallPower(emitter, reqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/tagFromBallUuid")
    public ResponseBodyEmitter getTagFromBallUuid(TagFromBallReqDto reqDto) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fTagService.getTagFromBallUuid(emitter,reqDto);
        return emitter;
    }
}
