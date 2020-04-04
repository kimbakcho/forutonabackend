package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserToPlayBallResDto {
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
    LocalDateTime joinTime;

    @QueryProjection
    public UserToPlayBallResDto(FBallPlayer fBallPlayer) {
        this.fBalluuid = fBallPlayer.getBallUuid().getBallUuid();
        this.fBallType = fBallPlayer.getBallUuid().getBallType();
        this.longitude = fBallPlayer.getBallUuid().getLongitude();
        this.latitude = fBallPlayer.getBallUuid().getLatitude();
        this.ballName = fBallPlayer.getBallUuid().getBallName();
        this.ballPlaceAddress = fBallPlayer.getBallUuid().getPlaceAddress();
        this.ballLikes = fBallPlayer.getBallUuid().getBallLikes();
        this.ballDisLikes = fBallPlayer.getBallUuid().getBallDisLikes();
        this.commentCount = fBallPlayer.getBallUuid().getCommentCount();
        this.activationTime = fBallPlayer.getBallUuid().getActivationTime();
        this.joinTime = fBallPlayer.getStartTime();
    }

}
