package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FBall.Domain.LikeActionType;
import lombok.Data;

@Data
public class FBallLikeReqDto {
    String valueUuid;
    String ballUuid;
    Long likePoint;
    Long disLikePoint;
    LikeActionType likeActionType;
}
