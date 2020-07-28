package com.wing.forutona.FBall.Service.BallListup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BallListUpServiceFactory {
    final BallListUpUserMakerBall ballListUpUserMakerBall;
    final BallListUpInfluencePower ballListUpInfluencePower;
    final BallListUpFromTagName ballListUpFromTagName;
    final BallListUpFromSearchTitle ballListUpFromSearchTitle;
    final BallListUpFromMapArea ballListUpFromMapArea;
    public BallListUpService create(String makeType) throws Exception {
        if(makeType.equals("ballListUpUserMakerBall")){
            return ballListUpUserMakerBall;
        }else if(makeType.equals("ballListUpInfluencePower")){
            return ballListUpInfluencePower;
        }else if(makeType.equals("ballListUpFromTagName")){
            return ballListUpFromTagName;
        }else if(makeType.equals("ballListUpFromSearchTitle")){
            return ballListUpFromSearchTitle;
        }else if(makeType.equals("ballListUpFromMapArea")){
            return ballListUpFromMapArea;
        }else {
            throw new Exception("Don't have Type BallListUpServiceFactory");
        }
    }
}
