package com.wing.forutona.App.FBallValuation.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.App.FBallValuation.Dto.FBallLikeReqDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallLikeResDto;
import com.wing.forutona.App.FBallValuation.Service.BallLikeService.BallLIkeServiceFactory;
import com.wing.forutona.App.FBallValuation.Service.BallLikeService.BallLikeStateSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FBallValuationController {
    final BallLIkeServiceFactory ballLIkeServiceFactory;
    final BallLikeStateSearchService ballLikeStateSearchService;

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBallValuation/BallLike")
    public FBallLikeResDto ballLike(@RequestBody FBallLikeReqDto reqDto, FFireBaseToken fireBaseToken) throws Exception {
        return ballLIkeServiceFactory.create(reqDto.getLikeActionType()).execute(reqDto,fireBaseToken.getUserFireBaseUid());
    }

    @GetMapping(value = "/v1/FBallValuation/BallLike")
    public FBallLikeResDto ballLike(String ballUuid,String uid) throws Exception {
        return ballLikeStateSearchService.getLikeState(ballUuid,uid);
    }

}
