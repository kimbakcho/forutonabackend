package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyUpdateReqDto;
import com.wing.forutona.FBall.Service.FBallReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class FBallReplyController {

    final FBallReplyService fBallReplyService;


    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBallReply")
    public ResponseBodyEmitter getFBallReply(FBallReplyReqDto reqDto, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fBallReplyService.getFBallReply(reqDto,pageable));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBallReply")
    public ResponseBodyEmitter insertFBallReply(@RequestBody FBallReplyInsertReqDto reqDto,FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fBallReplyService.insertFBallReply(fireBaseToken,reqDto));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FBallReply")
    public ResponseBodyEmitter updateFBallReply(@RequestBody FBallReplyUpdateReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fBallReplyService.updateFBallReply(fireBaseToken,reqDto));
                emitter.complete();
            } catch (Throwable e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;

    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBallReply/{replyUuid}")
    public ResponseBodyEmitter deleteFBallReply(@PathVariable String replyUuid,FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fBallReplyService.deleteFBallReply(fireBaseToken,replyUuid));
                emitter.complete();
            } catch (Throwable e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }


}
