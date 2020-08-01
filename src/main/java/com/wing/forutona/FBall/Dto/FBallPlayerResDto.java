package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class FBallPlayerResDto {
    private long idx;
    private FBallResDto ballUuid;
    private FUserInfoSimpleResDto playerUid;
    private Boolean hasLike;
    private Boolean hasDisLike;
    private Boolean hasGiveUp;
    private Boolean hasExit;
    private LocalDateTime startTime;
    private FBallPlayState playState;

    public FBallPlayerResDto(FBallPlayer fBallPlayer){
        this.idx = fBallPlayer.getIdx();
        this.ballUuid = new FBallResDto(fBallPlayer.getBallUuid());
        this.playerUid = new FUserInfoSimpleResDto(fBallPlayer.getPlayerUid());
        this.hasLike = fBallPlayer.getHasLike();
        this.hasDisLike = fBallPlayer.getHasDisLike();
        this.hasGiveUp = fBallPlayer.getHasGiveUp();
        this.hasExit = fBallPlayer.getHasExit();
        this.startTime = fBallPlayer.getStartTime();
        this.playState = fBallPlayer.getPlayState();
    }
}
