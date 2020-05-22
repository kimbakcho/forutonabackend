package com.wing.forutona.FBall.Service.FBallType;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Dto.FBallJoinReqDto;
import com.wing.forutona.FBall.Dto.FBallReqDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Component
public interface FBallTypeService<FBallInsertReqDto ,ReturnBallDto> {
    void insertBall(ResponseBodyEmitter emitter, FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken);
    void updateBall(ResponseBodyEmitter emitter,FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken);
    void selectBall(ResponseBodyEmitter emitter, FBallReqDto fBallReqDto);
    void deleteBall(ResponseBodyEmitter emitter,FBallReqDto fBallReqDto,FFireBaseToken fireBaseToken);
    void joinBall(ResponseBodyEmitter emitter, FBallJoinReqDto fBallReqDto, FFireBaseToken fireBaseToken);
    void ballHit(ResponseBodyEmitter emitter, FBallReqDto fBallReqDto);
}
