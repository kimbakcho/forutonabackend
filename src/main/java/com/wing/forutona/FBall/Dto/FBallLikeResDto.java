package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallLikeResDto {
    Long ballLike;
    Long ballDislike;
    Long likeServiceUseUserCount;
    Long ballPower;
    FBallValuationResDto fballValuationResDto;
}
