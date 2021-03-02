package com.wing.forutona.App.FBallValuation.Dto;

import com.wing.forutona.App.FBallValuation.Domain.LikeActionType;
import lombok.Data;

@Data
public class FBallVoteReqDto {
    String valueUuid;
    String ballUuid;
    Integer likePoint;
    Integer disLikePoint;
    LikeActionType likeActionType;
}
