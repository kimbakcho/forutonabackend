package com.wing.forutona.App.FBallValuation.Controller;

import com.wing.forutona.App.FBallValuation.Dto.FBallVoteReqDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteResDto;
import com.wing.forutona.App.FBallValuation.Service.BallLikeService.BallLIkeServiceFactory;
import com.wing.forutona.App.FBallValuation.Service.BallLikeService.BallVoteStateSearchService;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FBallValuationController {
    final BallLIkeServiceFactory ballLIkeServiceFactory;
    final BallVoteStateSearchService ballVoteStateSearchService;

    @PostMapping(value = "/v1/FBallValuation/BallVote")
    public FBallVoteResDto ballVote(@RequestBody FBallVoteReqDto reqDto, @AuthenticationPrincipal UserAdapter userAdapter) throws Exception {
        return ballLIkeServiceFactory.create(reqDto.getLikeActionType()).execute(reqDto,userAdapter.getfUserInfo().getUid());
    }

    @GetMapping(value = "/v1/FBallValuation/BallVote")
    public FBallVoteResDto getBallVoteState(String ballUuid, @AuthenticationPrincipal UserAdapter userAdapter) throws Exception {
        return ballVoteStateSearchService.getVoteState(ballUuid,userAdapter.getfUserInfo().getUid());
    }

}
