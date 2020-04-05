package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserToPlayBallResDto extends UserBallResDto {

    LocalDateTime joinTime;

    @QueryProjection
    public UserToPlayBallResDto(FBallPlayer fBallPlayer) {
        this.fBallUuid = fBallPlayer.getBallUuid().getBallUuid();
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
