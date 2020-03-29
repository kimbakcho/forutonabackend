package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class NearBallFindDistanceReqDto {
    private double latitude;
    private double longitude;
    private double distance;
}
