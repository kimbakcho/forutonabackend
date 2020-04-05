package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.MultiSort;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Service.FBallPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class FBallPlayerController {

    @Autowired
    FBallPlayerService fBallPlayerService;

    @GetMapping(value = "/v1/FBallPlayer/UserToPlayBallList")
    public ResponseBodyEmitter UserToPlayBallList(HttpServletResponse response, UserToPlayBallReqDto reqDto,
                                                  @RequestParam MultiSorts sorts,Pageable pageable){

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        fBallPlayerService.UserToPlayBallList(emitter,reqDto,sorts,pageable);
        return emitter;
    }

}
