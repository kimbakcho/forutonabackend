package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallValuationInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallValuationReqDto;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.FBall.Service.FBallValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class FBallValuationController {

    @Autowired
    FBallValuationService fBallValuationService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBallValuation")
    public ResponseBodyEmitter getFBallValuation(FBallValuationReqDto reqDto) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallValuationService.getFBallValuation(emitter,reqDto);
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/FBallValuation")
    public ResponseBodyEmitter insertFBallValuation(@RequestBody FBallValuationInsertReqDto reqDto,FFireBaseToken fireBaseToken) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallValuationService.insertFBallValuation(emitter,reqDto,fireBaseToken);
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FBallValuation")
    public ResponseBodyEmitter updateFBallValuation(@RequestBody FBallValuationInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallValuationService.updateFBallValuation(emitter,reqDto,fireBaseToken);
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBallValuation/{valueUuid}")
    public ResponseBodyEmitter deleteFBallValuation(@PathVariable String valueUuid,FFireBaseToken fireBaseToken) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallValuationService.deleteFBallValuation(emitter,valueUuid,fireBaseToken);
        return emitter;
    }
}
