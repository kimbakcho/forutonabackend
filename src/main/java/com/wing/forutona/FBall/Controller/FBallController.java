package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Service.FBallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.Map;

@RestController
public class FBallController {

    @Autowired
    FBallService fBallService;


    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/BallListUpFromMapArea")
    public ResponseBodyEmitter getListUpBallFromMapArea(BallFromMapAreaReqDto reqDto,
                                                        @RequestParam MultiSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUp(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/BallListUpFromSearchText")
    public ResponseBodyEmitter getListUpBallFromSearchText(BallNameSearchReqDto reqDto
            ,@RequestParam MultiSorts sorts, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUp(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/BallListUp")
    public ResponseBodyEmitter ListUpBall(FBallListUpReqDto reqDto, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUp(emitter,reqDto,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public ResponseBodyEmitter getUserToMakerBalls(UserToMakerBallReqDto reqDto,
                                                   @RequestParam MultiSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.getUserToMakerBalls(emitter,reqDto,sorts,pageable);
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/Insert")
    public ResponseBodyEmitter insertBall(@RequestBody FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.insertBall(emitter,reqDto,fireBaseToken);
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FBall/Update")
    public ResponseBodyEmitter updateBall(@RequestBody FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.updateBall(emitter,reqDto,fireBaseToken);
        return emitter;
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/BallImageUpload")
    public ResponseBodyEmitter ballImageUpload(@RequestParam("imageFiles[]") List<MultipartFile> files){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.ballImageUpload(emitter,files);
        return emitter;
    }

}
