package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Dto.FBallLikeResDto;
import com.wing.forutona.FBall.Service.BallLikeService.BallLIkeServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FBallValuationController {
    final BallLIkeServiceFactory ballLIkeServiceFactory;

    @AuthFireBaseJwtCheck
    @PostMapping(value = "/v1/FBallValuation/BallLike")
    public FBallLikeResDto ballLike(@RequestBody FBallLikeReqDto reqDto, FFireBaseToken fireBaseToken) throws Exception {
        return ballLIkeServiceFactory.create(reqDto.getLikeActionType()).execute(reqDto,fireBaseToken.getUserFireBaseUid());
    }

}
