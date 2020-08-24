package com.wing.forutona.FBallValuation.Dto;

import com.wing.forutona.FBallValuation.Domain.LikeActionType;
import lombok.Data;

@Data
public class FBallLikeReqDto {
    String valueUuid;
    String ballUuid;
    Long likePoint;
    Long disLikePoint;
    LikeActionType likeActionType;
}
