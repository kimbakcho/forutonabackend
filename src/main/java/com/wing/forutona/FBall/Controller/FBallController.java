package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.*;
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


    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/BallListUpFromMapArea")
    public ResponseBodyEmitter getListUpBallFromMapArea(BallFromMapAreaReqDto reqDto,
                                                        @RequestParam MultiSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.getListUpBallFromMapArea(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/BallListUpFromSearchText")
    public ResponseBodyEmitter getListUpBallFromSearchText(BallNameSearchReqDto reqDto
            ,@RequestParam MultiSorts sorts, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.getListUpBallFromSearchText(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/BallListUp")
    public ResponseBodyEmitter ListUpBall(FBallListUpReqDto reqDto, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUp(emitter,reqDto,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public ResponseBodyEmitter getUserToMakerBalls(UserToMakerBallReqDto reqDto,
                                                   @RequestParam MultiSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.getUserToMakerBalls(emitter,reqDto,sorts,pageable);
        return emitter;
    }

}
