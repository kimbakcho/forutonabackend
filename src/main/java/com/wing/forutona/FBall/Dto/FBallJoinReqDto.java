package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FBall.Domain.FBallType;
import lombok.Data;

@Data
public class FBallJoinReqDto {
    FBallType ballType;
    String ballUuid;
    String playerUid;
}
