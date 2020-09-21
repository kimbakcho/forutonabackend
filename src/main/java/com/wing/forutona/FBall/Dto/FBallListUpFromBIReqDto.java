package com.wing.forutona.FBall.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FBallListUpFromBIReqDto {
    double userLatitude;
    double userLongitude;
    double mapCenterLatitude;
    double mapCenterLongitude;
}
