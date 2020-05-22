package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallJoinReqDto {
    FBallType ballType;
    String ballUuid;
    String playerUid;
}
