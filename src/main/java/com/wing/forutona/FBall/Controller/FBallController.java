package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Service.FBallService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FBallController {


    final FBallService fBallService;


    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/ListUpFromMapArea")
    public ResponseBodyEmitter getListUpBallFromMapArea(BallFromMapAreaReqDto reqDto,
                                                        @RequestParam FSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUpFromMapArea(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/ListUpFromSearchTitle")
    public ResponseBodyEmitter getListUpBallFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto
            , @RequestParam FSorts sorts, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUpFromSearchTitle(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/ListUpFromBallInfluencePower")
    public ResponseBodyEmitter ListUpBallInfluencePower(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.BallListUpFromBallInfluencePower(emitter,reqDto,pageable);
        return emitter;
    }


    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/ListUpFromTagName")
    public ResponseBodyEmitter ListUpFromTagName(FBallListUpFromTagReqDto reqDto,
                                                 @RequestParam FSorts sorts, Pageable pageable) throws ParseException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.ListUpFromTagName(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public ResponseBodyEmitter getUserToMakerBalls(UserToMakerBallReqDto reqDto,
                                                   @RequestParam FSorts sorts, Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.getUserToMakerBalls(emitter,reqDto,sorts,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FBall/UserToMakerBall")
    public ResponseBodyEmitter getUserToMakerBall(UserToMakerBallSelectReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fBallService.getUserToMakerBall(emitter,reqDto);
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
