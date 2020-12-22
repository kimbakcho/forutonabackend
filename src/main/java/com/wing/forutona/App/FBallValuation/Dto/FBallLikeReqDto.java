package com.wing.forutona.App.FBallValuation.Dto;

import com.wing.forutona.App.FBallValuation.Domain.LikeActionType;
import lombok.Data;

@Data
public class FBallLikeReqDto {
    String valueUuid;
    String ballUuid;
    Long likePoint;
    Long disLikePoint;
    LikeActionType likeActionType;
}
