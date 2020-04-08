package com.wing.forutona.FTag.Controller;

import com.google.firebase.auth.FirebaseToken;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FTag.Dto.TagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingReqDto;
import com.wing.forutona.FTag.Dto.TagSearchFromTextReqDto;
import com.wing.forutona.FTag.Service.FTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

//해당 콘트롤러는 RankingSystem을 책임 진다/
@RestController
public class FTagController {

    @Autowired
    FTagService fTagService;

    @Autowired
    FireBaseAdmin fireBaseAdmin;


    @GetMapping(value = "/v1/FTag/Ranking")
    public ResponseBodyEmitter getFTagRanking(HttpServletResponse response, TagRankingReqDto tagRankingReqDto) throws ParseException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        ResponseBodyEmitter emitter  = new ResponseBodyEmitter();
        fTagService.getFTagRanking( emitter, tagRankingReqDto);
        return emitter;
    }

        @GetMapping(value = "/v1/FTag/tagSearchFromTextToBalls")
        public ResponseBodyEmitter getTagSearchFromTextToBalls(HttpServletResponse response, TagSearchFromTextReqDto reqDto,
                @RequestParam MultiSorts sorts, Pageable pageable){
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            ResponseBodyEmitter emitter  = new ResponseBodyEmitter();
            fTagService.getTagSearchFromTextToBalls(emitter,reqDto,sorts,pageable);
            return emitter;
        }

        @GetMapping(value = "/v1/FTag/tagSearchFromTextToTagRankings")
        public ResponseBodyEmitter getTagSearchFromTextToTagRankings(HttpServletResponse response, TagSearchFromTextReqDto reqDto){
            response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        ResponseBodyEmitter emitter  = new ResponseBodyEmitter();
        fTagService.getTagSearchFromTextToTagRankings(emitter,reqDto);
        return emitter;
    }


}
