package com.wing.forutona.App.FBallReply.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyReqDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyResDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyUpdateReqDto;
import com.wing.forutona.App.FBallReply.Service.FBallReplyInsertServiceFactory;
import com.wing.forutona.App.FBallReply.Service.FBallReplyService;
import com.wing.forutona.App.FireBaseMessage.Service.FBallReplyFCMServiceFactory;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FBallReplyController {

    final FBallReplyService fBallReplyService;

    final FBallReplyInsertServiceFactory fBallReplyInsertServiceFactory;

    final FBallReplyFCMServiceFactory fBallReplyFCMServiceFactory;


    @GetMapping(value = "/v1/FBallReply")
    public Page<FBallReplyResDto> getFBallReply(FBallReplyReqDto reqDto, Pageable pageable) {
        return fBallReplyService.getFBallReply(reqDto, pageable);
    }

    @GetMapping(value = "/v1/FBallReply/Count")
    public Long getFBallReply(String ballUuid) {
        return fBallReplyService.getFBallReplyCount(ballUuid);
    }


    @PostMapping(value = "/v1/FBallReply")
    public FBallReplyResDto insertFBallReply(@RequestBody FBallReplyInsertReqDto reqDto, @AuthenticationPrincipal UserAdapter userAdapter ) throws JsonProcessingException, FirebaseMessagingException {
        return fBallReplyService.insertFBallReply(userAdapter,
                fBallReplyInsertServiceFactory.getFBallReplyInsertServiceFactory(reqDto),
                fBallReplyFCMServiceFactory.getFBallReplyFCMServiceFactory(reqDto),
                reqDto);
    }


    @PutMapping(value = "/v1/FBallReply")
    public FBallReplyResDto updateFBallReply(@RequestBody FBallReplyUpdateReqDto reqDto, @AuthenticationPrincipal UserAdapter userAdapter) throws Throwable {
        return fBallReplyService.updateFBallReply(userAdapter, reqDto);
    }


    @DeleteMapping(value = "/v1/FBallReply/{replyUuid}")
    public FBallReplyResDto deleteFBallReply(@PathVariable String replyUuid, @AuthenticationPrincipal UserAdapter userAdapter) throws Throwable {
        return fBallReplyService.deleteFBallReply(userAdapter, replyUuid);
    }

}
