package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserToPlayBallResDto extends FBallResDto {

    LocalDateTime joinTime;

    @QueryProjection
    public UserToPlayBallResDto(FBall fBall,FBallPlayer fBallPlayer) {
        super(fBall);
        joinTime = fBallPlayer.getStartTime();
    }

}
