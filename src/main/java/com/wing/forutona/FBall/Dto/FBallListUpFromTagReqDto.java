package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallListUpFromTagReqDto {
    String searchTag;
    double latitude;
    double longitude;
}
