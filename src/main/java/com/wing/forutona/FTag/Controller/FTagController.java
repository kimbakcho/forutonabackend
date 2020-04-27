package com.wing.forutona.FTag.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FTag.Dto.TagFromBallReqDto;
import com.wing.forutona.FTag.Dto.TagRankingReqDto;
import com.wing.forutona.FTag.Dto.TagSearchFromTextReqDto;
import com.wing.forutona.FTag.Service.FTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

//해당 콘트롤러는 RankingSystem을 책임 진다/
@RestController
public class FTagController {

    @Autowired
    FTagService fTagService;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/Ranking")
    public ResponseBodyEmitter getFTagRanking(TagRankingReqDto tagRankingReqDto) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fTagService.getFTagRanking(emitter, tagRankingReqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/tagSearchFromTextToBalls")
    public ResponseBodyEmitter getTagSearchFromTextToBalls(TagSearchFromTextReqDto reqDto,
                                                           @RequestParam MultiSorts sorts, Pageable pageable) {

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fTagService.getTagSearchFromTextToBalls(emitter, reqDto, sorts, pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FTag/tagSearchFromTextToTagRankings")
    public ResponseBodyEmitter getTagSearchFromTextToTagRankings(TagSearchFromTextReqDto reqDto) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fTagService.getTagSearchFromTextToTagRankings(emitter, reqDto);
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
