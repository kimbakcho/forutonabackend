package com.wing.forutona.App.FBall.Controller;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.App.FBall.Dto.*;
import com.wing.forutona.App.FBall.Service.*;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FBallController {

    final BallHitService ballHitService;

    final BallListUpService ballListUpService;

    final BallImageUploadService ballImageUploadService;

    final BallInsertService ballInsertService;

    final BallUpdateService ballUpdateService;

    final BallSelectService ballSelectService;

    final BallDeleteService ballDeleteService;

    @GetMapping(value = "/v1/FBall/ListUpFromMapArea")
    public Page<FBallResDto> getListUpBallFromMapArea(BallFromMapAreaReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpFromMapAreaOrderByBP(reqDto, pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromSearchTitle")
    public Page<FBallResDto> getListUpBallFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto
            , Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpFromSearchTitle(reqDto, pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpFromTagName")
    public Page<FBallResDto> ListUpFromTagName(FBallListUpFromTagReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpFromTagName(reqDto, pageable);
    }

    @GetMapping(value = "/v1/FBall/UserToMakerBalls")
    public Page<FBallResDto> searchUserToMakerBalls(String makerUid, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpUserMakerBall(makerUid, pageable);
    }

    @GetMapping(value = "/v1/FBall/ListUpBallListUpOrderByBI")
    public Page<FBallResDto> searchBallListUpOrderByBI(FBallListUpFromBIReqDto reqDto, Pageable pageable) throws Exception {
        return ballListUpService.searchBallListUpOrderByBI(reqDto, pageable);
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/FBall/BallImageUpload")
    public FBallImageUploadResDto ballImageUpload(@RequestParam("imageFiles[]") List<MultipartFile> files) throws IOException {
        FBallImageUploadResDto fBallImageUploadResDto = ballImageUploadService.ballImageUpload(files);
        return fBallImageUploadResDto;
    }

    @PostMapping(value = "/v1/FBall")
    public FBallResDto insertBall(@RequestBody FBallInsertReqDto reqDto, @AuthenticationPrincipal UserAdapter userAdapter) throws ParseException {
        return ballInsertService.insertBall(reqDto, userAdapter.getfUserInfo().getUid());
    }

    @PutMapping(value = "/v1/FBall")
    public FBallResDto updateBall(@RequestBody FBallUpdateReqDto reqDto, @AuthenticationPrincipal UserAdapter userAdapter) throws Exception {
        return ballUpdateService.updateBall(reqDto, userAdapter.getfUserInfo().getUid());
    }

    @GetMapping(value = "/v1/FBall")
    public FBallResDto selectBall(String ballUuid) {
        return ballSelectService.selectBall(ballUuid);
    }

    @GetMapping(value = "/v1/FBalls")
    public List<FBallResDto> selectBalls(@RequestParam(value = "ballUuids") List<String> ballUuids) {
        return ballSelectService.selectBalls(ballUuids);
    }

    @DeleteMapping(value = "/v1/FBall")
    public String deleteBall(String ballUuid, @AuthenticationPrincipal UserAdapter userAdapter) throws Exception {
        return ballDeleteService.deleteBall(ballUuid, userAdapter.getfUserInfo().getUid());
    }

    @PostMapping(value = "/v1/FBall/BallHit")
    public Long ballHit(String ballUuid) {
        return ballHitService.hit(ballUuid);
    }


}
