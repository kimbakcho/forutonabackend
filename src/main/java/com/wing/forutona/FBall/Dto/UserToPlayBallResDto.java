package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserToPlayBallResDto extends UserBallResDto {

    LocalDateTime joinTime;

    @QueryProjection
    public UserToPlayBallResDto(FBallPlayer fBallPlayer) {

        this.fBallUuid = getFBall(fBallPlayer).getBallUuid();
        this.fBallType = getFBall(fBallPlayer).getBallType();
        this.longitude = getFBall(fBallPlayer).getLongitude();
        this.latitude = getFBall(fBallPlayer).getLatitude();
        this.ballName = getFBall(fBallPlayer).getBallName();
        this.ballPlaceAddress = getFBall(fBallPlayer).getPlaceAddress();
        this.ballLikes = getFBall(fBallPlayer).getBallLikes();
        this.ballDisLikes = getFBall(fBallPlayer).getBallDisLikes();
        this.commentCount = getFBall(fBallPlayer).getCommentCount();
        this.activationTime = getFBall(fBallPlayer).getActivationTime();
        this.joinTime = fBallPlayer.getStartTime();
        this.ballDeleteFlag = getFBall(fBallPlayer).isBallDeleteFlag();
        this.ballUid = getFBall(fBallPlayer).getFBallUid().getUid();

    }

    public FBall getFBall(FBallPlayer fBallPlayer) {
        return fBallPlayer.getBallUuid();
    }

}
