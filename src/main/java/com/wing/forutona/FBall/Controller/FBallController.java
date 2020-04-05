package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Dto.FBallListUpReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Dto.QFBallResDto;
import com.wing.forutona.FBall.Dto.UserToMakerBallReqDto;
import com.wing.forutona.FBall.Service.FBallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class FBallController {

    @Autowired
    FBallService fBallService;

    @GetMapping(value = "/v1/FBall/BallListUp")
    public ResponseBodyEmitter ListUpBall(HttpServletResponse response, FBallListUpReqDto reqDto, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        fBallService.BallListUp(emitter,reqDto,pageable);
        return emitter;
    }

    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public ResponseBodyEmitter getUserToMakerBalls(HttpServletResponse response, UserToMakerBallReqDto reqDto,
                                                   @RequestParam MultiSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        fBallService.getUserToMakerBalls(emitter,reqDto,sorts,pageable);
        return emitter;
    }
}
