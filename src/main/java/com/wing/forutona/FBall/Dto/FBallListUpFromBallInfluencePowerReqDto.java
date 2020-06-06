package com.wing.forutona.FBall.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FBallListUpFromBallInfluencePowerReqDto {
    double latitude;
    double longitude;
    int ballLimit;
}
