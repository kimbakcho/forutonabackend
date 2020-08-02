package com.wing.forutona.FBall.Controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.FBallReplyUpdateReqDto;
import com.wing.forutona.FBall.Service.FBallReply.FBallReplyInsertServiceFactory;
import com.wing.forutona.FBall.Service.FBallReply.FBallReplyService;
import com.wing.forutona.FireBaseMessage.Service.FBallReplyFCMServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    final FBallReplyInsertServiceFactory fBallReplyInsertServiceFactory;

    final FBallReplyFCMServiceFactory fBallReplyFCMServiceFactory;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBallReply")
    public Page<FBallReplyResDto> getFBallReply(FBallReplyReqDto reqDto, Pageable pageable){
        return fBallReplyService.getFBallReply(reqDto,pageable);
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBallReply")
    public ResponseBodyEmitter insertFBallReply(@RequestBody FBallReplyInsertReqDto reqDto,FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fBallReplyService.insertFBallReply(fireBaseToken,
                        fBallReplyInsertServiceFactory.getFBallReplyInsertServiceFactory(reqDto),
                        fBallReplyFCMServiceFactory.getFBallReplyFCMServiceFactory(reqDto),
                        reqDto));
                emitter.complete();
            } catch (IOException | FirebaseMessagingException e) {
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
