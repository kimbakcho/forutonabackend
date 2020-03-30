package com.wing.forutona.FBall.Controller;

import com.wing.forutona.FBall.Dto.FBallListUpReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Dto.QFBallResDto;
import com.wing.forutona.FBall.Service.FBallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FBallController {

    @Autowired
    FBallService fBallService;

    @GetMapping(value = "/v1/FBall/BallListUp")
    public FBallListUpWrapDto ListUpBall(FBallListUpReqDto reqDto, Pageable pageable){
        return fBallService.BallListUp(reqDto,pageable);
    }
}
