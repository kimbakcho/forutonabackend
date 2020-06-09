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
    public UserToPlayBallResDto(FBallPlayer fBallPlayer) {
        super(fBallPlayer.getBallUuid());
        joinTime = fBallPlayer.getStartTime();
    }

}
