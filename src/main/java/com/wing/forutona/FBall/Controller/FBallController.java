package com.wing.forutona.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Service.*;
import com.wing.forutona.FBall.Service.BallListup.BallListUpServiceFactory;
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

    final BallListUpServiceFactory ballListUpServiceFactory;

    final BallImageUploadService ballImageUploadService;

    final BallInsertService ballInsertService;

    final BallUpdateService ballUpdateService;

    final  BallSelectService ballSelectService;

    final  BallDeleteService ballDeleteService;

    final BallLikeService ballLikeService;

    final BallDisLikeService ballDisLikeService;

    final BallCancelLikeService ballCancelLikeService;

    @GetMapping(value = "/v1/FBall/ListUpFromMapArea")
    public Page<FBallResDto> getListUpBallFromMapArea(@RequestParam BallFromMapAreaReqDto reqDto,
                                                      @RequestParam FSorts sorts, Pageable pageable) throws Exception {
        return ballListUpServiceFactory.create("ballListUpFromMapArea").search(reqDto,sorts,pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromSearchTitle")
    public Page<FBallResDto>  getListUpBallFromSearchTitle(@RequestParam FBallListUpFromSearchTitleReqDto reqDto
            , @RequestParam FSorts sorts, Pageable pageable) throws Exception {
        return ballListUpServiceFactory.create("ballListUpFromSearchTitle").search(reqDto,sorts,pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromTagName")
    public Page<FBallResDto> ListUpFromTagName(@RequestParam FBallListUpFromTagReqDto reqDto,
                                                 @RequestParam FSorts sorts, Pageable pageable) throws Exception {
        return ballListUpServiceFactory.create("ballListUpFromTagName").search(reqDto,sorts,pageable);
    }

    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public Page<FBallResDto> getUserToMakerBalls(@RequestParam String makerUid,
                                                   @RequestParam FSorts sorts, Pageable pageable) throws Exception {
        return ballListUpServiceFactory.create("ballListUpUserMakerBall").search(makerUid,sorts,pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromBallInfluencePower")
    public Page<FBallResDto> ListUpBallInfluencePower(@RequestParam FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpServiceFactory.create("ballListUpInfluencePower").search(reqDto,null,pageable);
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
    public FBallResDto selectBall(@RequestParam String ballUuid){
        return ballSelectService.selectBall(ballUuid);
    }

    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBall")
    public String deleteBall(@RequestParam String ballUuid,FFireBaseToken fireBaseToken) throws Exception {
        return ballDeleteService.deleteBall(ballUuid,fireBaseToken.getUserFireBaseUid());
    }

    @PostMapping(value = "/v1/FBall/BallHit")
    public Long deleteBall(@RequestParam String ballUuid){
        return ballHitService.hit(ballUuid);
    }

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/Like")
    public Integer ballLike(@RequestBody FBallLikeReqDto reqDto,FFireBaseToken fireBaseToken) throws Exception {
        return ballLikeService.likeExecute(reqDto,fireBaseToken.getUserFireBaseUid());
    }

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBall/DisLike")
    public Integer ballDisLike(@RequestBody FBallDisLikeReqDto reqDto,FFireBaseToken fireBaseToken) throws Exception {
        return ballDisLikeService.disLikeExecute(reqDto,fireBaseToken.getUserFireBaseUid());
    }

    @AuthFireBaseJwtCheck
    @DeleteMapping(value = "/v1/FBall/CancelLike")
    public void ballCancelLike(@RequestParam String ballUuid,FFireBaseToken fireBaseToken) throws Exception {
         ballCancelLikeService.cancelExecute(ballUuid,fireBaseToken.getUserFireBaseUid());
    }

}
