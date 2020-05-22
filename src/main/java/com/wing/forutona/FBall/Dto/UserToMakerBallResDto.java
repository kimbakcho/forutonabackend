package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserToMakerBallResDto extends UserBallResDto{

    @QueryProjection
    public UserToMakerBallResDto(FBall fBall){
        this.fBallUuid = fBall.getBallUuid();
        this.fBallType = fBall.getBallType();
        this.longitude = fBall.getLongitude();
        this.latitude = fBall.getLatitude();
        this.ballName = fBall.getBallName();
        this.ballPlaceAddress = fBall.getPlaceAddress();
        this.ballLikes = fBall.getBallLikes();
        this.ballDisLikes = fBall.getBallDisLikes();
        this.commentCount = fBall.getCommentCount();
        this.activationTime = fBall.getActivationTime();
        this.makeTime =fBall.getMakeTime();
        this.ballDeleteFlag = fBall.isBallDeleteFlag();
        this.ballUid = fBall.getFBallUid().getUid();
    }
}
