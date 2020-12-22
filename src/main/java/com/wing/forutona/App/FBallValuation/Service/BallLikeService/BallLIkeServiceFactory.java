package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBallValuation.Domain.LikeActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BallLIkeServiceFactory {
    final BallLikeServiceImpl ballLikeService;
    final BallDisLikeServiceImpl ballDisLikeService;
    final BallLikeCancelServiceImpl ballLikeCancelService;

    public BallLikeService create(LikeActionType serviceType) throws Exception {
        if(LikeActionType.LIKE.equals(serviceType)){
            return ballLikeService;
        }else if (LikeActionType.DISLIKE.equals(serviceType)){
            return ballDisLikeService;
        }else if(LikeActionType.CANCEL.equals(serviceType)){
            return ballLikeCancelService;
        } else {
            throw new Exception("Don't have Service BallLIkeServiceFactory");
        }
    }
}
