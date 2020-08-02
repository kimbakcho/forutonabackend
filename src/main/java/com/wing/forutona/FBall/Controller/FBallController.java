package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Service.*;
import com.wing.forutona.FBall.Service.BallLikeService.BallLIkeServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class FBallController {

    final BallHitService ballHitService;

    final BallListUpService ballListUpService;

    final BallImageUploadService ballImageUploadService;

    final BallInsertService ballInsertService;

    final BallUpdateService ballUpdateService;

    final  BallSelectService ballSelectService;

    final  BallDeleteService ballDeleteService;



    @GetMapping(value = "/v1/FBall/ListUpFromMapArea")
    public Page<FBallResDto> getListUpBallFromMapArea( BallFromMapAreaReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpFromMapArea(reqDto,pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromSearchTitle")
    public Page<FBallResDto>  getListUpBallFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto
            , Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpFromSearchTitle(reqDto,pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromTagName")
    public Page<FBallResDto> ListUpFromTagName(FBallListUpFromTagReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpFromTagName(reqDto,pageable);
    }

    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public Page<FBallResDto> searchUserToMakerBalls(String makerUid, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpUserMakerBall(makerUid,pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromBallInfluencePower")
    public Page<FBallResDto> ListUpBallInfluencePower(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpInfluencePower(reqDto,pageable);
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/BallImageUpload")
    public ResponseBodyEmitter ballImageUpload(@RequestParam("imageFiles[]") List<MultipartFile> files){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(ballImageUploadService.ballImageUpload(files));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall")
    public FBallResDto insertBall(@RequestBody FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) throws ParseException {
       return ballInsertService.insertBall(reqDto,fireBaseToken.getUserFireBaseUid());
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FBall")
    public FBallResDto updateBall(@RequestBody FBallUpdateReqDto reqDto, FFireBaseToken fireBaseToken) throws Exception {
        return ballUpdateService.updateBall(reqDto,fireBaseToken.getUserFireBaseUid());
    }

    @GetMapping(value = "/v1/FBall")
    public FBallResDto selectBall(String ballUuid){
        return ballSelectService.selectBall(ballUuid);
    }

    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBall")
    public String deleteBall(String ballUuid,FFireBaseToken fireBaseToken) throws Exception {
        return ballDeleteService.deleteBall(ballUuid,fireBaseToken.getUserFireBaseUid());
    }

    @PostMapping(value = "/v1/FBall/BallHit")
    public Long deleteBall(String ballUuid){
        return ballHitService.hit(ballUuid);
    }



}
