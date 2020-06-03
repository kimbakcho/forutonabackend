package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallJoinReqDto;
import com.wing.forutona.FBall.Dto.FBallReqDto;
import com.wing.forutona.FBall.Service.FBallType.IssueBallTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequiredArgsConstructor
public class IssueBallController {

    final IssueBallTypeService issueBallTypeService;

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/FBall/Issue/Select")
    public ResponseBodyEmitter selectBall(FBallReqDto fBallReqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        issueBallTypeService.selectBall(emitter,fBallReqDto);
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/Issue/Insert")
    public ResponseBodyEmitter insertBall(@RequestBody FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        issueBallTypeService.insertBall(emitter,reqDto,fireBaseToken);
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/Issue/Join")
    public ResponseBodyEmitter joinBall(@RequestBody FBallJoinReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        issueBallTypeService.joinBall(emitter,reqDto,fireBaseToken);
        return emitter;
    }

    @PostMapping(value = "/v1/FBall/Issue/BallHit")
    public ResponseBodyEmitter BallHit(@RequestBody FBallReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        issueBallTypeService.ballHit(emitter,reqDto);
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FBall/Issue/Update")
    public ResponseBodyEmitter updateBall(@RequestBody FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        issueBallTypeService.updateBall(emitter,reqDto,fireBaseToken);
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBall/Issue/Delete")
    public ResponseBodyEmitter deleteBall(FBallReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        issueBallTypeService.deleteBall(emitter,reqDto,fireBaseToken);
        return emitter;
    }

}
