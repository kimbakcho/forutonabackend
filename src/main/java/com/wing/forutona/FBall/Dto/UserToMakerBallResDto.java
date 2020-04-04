package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserToMakerBallResDto {
    String fBalluuid;
    FBallType fBallType;
    double longitude;
    double latitude;
    String ballName;
    String ballPlaceAddress;
    //ball의 Like
    long ballLikes;
    //ball의 DisLike
    long ballDisLikes;
    long commentCount;
    LocalDateTime activationTime;
    LocalDateTime makeTime;

    @QueryProjection
    public UserToMakerBallResDto(FBall fBall){
        this.fBalluuid = fBall.getBallUuid();
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
    }
}
