package com.wing.forutona.FBall.Service.BallMaker;

import com.wing.forutona.FBall.Dto.FBallType;

public class FBallMakerFactory {
    public static FBallMakerService createFBallMakerService(FBallType fBallType){
        if(fBallType.equals(FBallType.IssueBall)){
            return new IssueBallMakerService();
        }else {
            return null;
        }
    }
}
