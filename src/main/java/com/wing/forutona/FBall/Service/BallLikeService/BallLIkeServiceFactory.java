package com.wing.forutona.FBall.Service.BallLikeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BallLIkeServiceFactory {
    final BallLikeServiceImpl ballLikeService;
    final BallDisLikeServiceImpl ballDisLikeService;
    final BallLikeCancelServiceImpl ballLikeCancelService;

    public BallLikeService create(String serviceType) throws Exception {
        if(serviceType.equals("Like")){
            return ballLikeService;
        }else if (serviceType.equals("DisLike")){
            return ballDisLikeService;
        }else if(serviceType.equals("Cancel")){
            return ballLikeCancelService;
        } else {
            throw new Exception("Don't have Service BallLIkeServiceFactory");
        }
    }
}
