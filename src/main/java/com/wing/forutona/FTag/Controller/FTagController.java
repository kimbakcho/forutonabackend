package com.wing.forutona.FTag.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.FireBaseAdmin;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FTag.Dto.RelationTagRankingFromTagNameReqDto;
import com.wing.forutona.FTag.Dto.TagFromBallReqDto;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpFromTagReqDto;
import com.wing.forutona.FTag.Dto.TagResDto;
import com.wing.forutona.FTag.Service.FTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class FTagController {

    @Autowired
    FTagService fTagService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/RankingFromBallInfluencePower")
    public ResponseBodyEmitter getFTagRankingFromBallInfluencePower(TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fTagService.getFTagRankingFromBallInfluencePower(tagRankingFromBallInfluencePowerReqDto));
                emitter.complete();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/RelationTagRankingFromTagNameOrderByBallPower")
    public ResponseBodyEmitter getRelationTagRankingFromTagNameOrderByBallPower(RelationTagRankingFromTagNameReqDto reqDto) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fTagService.getRelationTagRankingFromTagNameOrderByBallPower( reqDto));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/tagFromBallUuid")
    public List<TagResDto> getTagFromBallUuid(TagFromBallReqDto reqDto) {
        return fTagService.getTagFromBallUuid(reqDto);
    }
}
