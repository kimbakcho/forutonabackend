package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallListUpFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Service.FBallListUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class FBallListUpController {

    @Autowired
    FBallListUpService fBallListUpService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/ListUpFromBallInfluencePower")
    public ResponseBodyEmitter ListUpBallInfluencePower(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fBallListUpService.ListUpBallInfluencePower(reqDto,pageable));
                emitter.complete();
            } catch (IOException | ParseException e ) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
