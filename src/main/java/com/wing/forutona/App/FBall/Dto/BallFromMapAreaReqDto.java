package com.wing.forutona.App.FBall.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BallFromMapAreaReqDto {
    double southwestLat;
    double southwestLng;
    double northeastLat;
    double northeastLng;
    double centerPointLat;
    double centerPointLng;
}
