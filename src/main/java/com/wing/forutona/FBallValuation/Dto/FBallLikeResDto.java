package com.wing.forutona.FBallValuation.Dto;

import lombok.Data;

@Data
public class FBallLikeResDto {
    Long ballLike;
    Long ballDislike;
    Long likeServiceUseUserCount;
    Long ballPower;
    FBallValuationResDto fballValuationResDto;
}
