package com.wing.forutona.FTag.Controller;

import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.FTag.Dto.TagRankingDto;
import com.wing.forutona.FTag.Dto.TagRankingReqDto;
import com.wing.forutona.FTag.Service.FTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

//해당 콘트롤러는 RankingSystem을 책임 진다/
@RestController
public class FTagController {

    @Autowired
    FTagService fTagService;

    @Autowired
    FireBaseAdmin fireBaseAdmin;


    @GetMapping(value = "/v1/FTag/Ranking")
    public ResponseBodyEmitter getFTagRanking(TagRankingReqDto tagRankingReqDto){
        ResponseBodyEmitter emitter  = new ResponseBodyEmitter();
        fTagService.getFTagRanking( emitter, tagRankingReqDto);
        return emitter;
    }
}
