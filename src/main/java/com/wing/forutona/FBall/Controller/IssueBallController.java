package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.IssueBallInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallJoinReqDto;
import com.wing.forutona.FBall.Dto.FBallReqDto;
import com.wing.forutona.FBall.Dto.IssueBallUpdateReqDto;
import com.wing.forutona.FBall.Service.FBallType.IssueBallTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class IssueBallController {

    final IssueBallTypeService issueBallTypeService;

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/FBall/Issue/Select")
    public ResponseBodyEmitter selectBall(FBallReqDto fBallReqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(issueBallTypeService.selectBall(fBallReqDto));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/FBall/Issue/Insert")
    public ResponseBodyEmitter insertBall(@RequestBody IssueBallInsertReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(issueBallTypeService.insertBall(reqDto,fireBaseToken));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/Issue/Join")
    public ResponseBodyEmitter joinBall(@RequestBody FBallJoinReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {

                emitter.send(issueBallTypeService.joinBall(reqDto,fireBaseToken));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @PostMapping(value = "/v1/FBall/Issue/BallHit")
    public ResponseBodyEmitter BallHit(@RequestBody FBallReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(issueBallTypeService.ballHit(reqDto));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FBall/Issue/Update")
    public ResponseBodyEmitter updateBall(@RequestBody IssueBallUpdateReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(issueBallTypeService.updateBall(reqDto,fireBaseToken));
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBall/Issue/Delete")
    public ResponseBodyEmitter deleteBall(FBallReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {

                emitter.send(issueBallTypeService.deleteBall(reqDto,fireBaseToken));
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;

    }

}
