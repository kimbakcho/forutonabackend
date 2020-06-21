package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.UserPositionUpdateReqDto;
import com.wing.forutona.ForutonaUser.Service.UserPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class FUserPositionController {

    @Autowired
    UserPositionService userPositionService;

    @ResponseAddJsonHeader
    @PutMapping(value = "/v1/ForutonaUser/UserPosition")
    public ResponseBodyEmitter updateUserPosition(@RequestBody UserPositionUpdateReqDto reqDto, FFireBaseToken fFireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try {
                int result = userPositionService.updateUserPosition(reqDto, fFireBaseToken);
                emitter.send(result);
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
