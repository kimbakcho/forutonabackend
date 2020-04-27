package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Service.FBallReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class FBallReplyController {


    @Autowired
    FBallReplyService fBallReplyService;

    /**
     * Detail이 fasle 이면 대표 댓글만 아니면 상세 댓글만 리턴 해줌
     * @param reqDto
     * @param pageable
     * @return
     */
    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBallReply")
    public ResponseBodyEmitter getFBallReply(FBallReplyReqDto reqDto, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(!reqDto.isDetail()){
            fBallReplyService.getFBallReply(emitter,reqDto,pageable);
        }else {
            fBallReplyService.getFBallDetailReply(emitter,reqDto);
        }
        return emitter;
    }


    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBallReply")
    public ResponseBodyEmitter insertFBallReply(@RequestBody FBallReplyInsertReqDto reqDto,FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallReplyService.insertFBallReply(emitter,fireBaseToken,reqDto);
        return emitter;
    }



}
