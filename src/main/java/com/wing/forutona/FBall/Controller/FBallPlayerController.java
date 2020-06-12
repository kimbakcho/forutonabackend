package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallSelectReqDto;
import com.wing.forutona.FBall.Service.FBallPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class FBallPlayerController {

    @Autowired
    FBallPlayerService fBallPlayerService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBallPlayer/UserToPlayBallList")
    public ResponseBodyEmitter UserToPlayBallList(UserToPlayBallReqDto reqDto,
                                                  @RequestParam FSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallPlayerService.UserToPlayBallList(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBallPlayer/UserToPlayBall")
    public ResponseBodyEmitter UserToPlayBall(UserToPlayBallSelectReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallPlayerService.UserToPlayBall(emitter,reqDto);
        return emitter;
    }

}
