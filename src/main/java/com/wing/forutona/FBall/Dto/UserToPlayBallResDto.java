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
        fballResDto = new FBallResDto(fBallPlayer.getBallUuid());
        joinTime = fBallPlayer.getStartTime();
    }

    public FBall getFBall(FBallPlayer fBallPlayer) {
        return fBallPlayer.getBallUuid();
    }

}
