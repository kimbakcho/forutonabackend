package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class BallNameSearchReqDto {
    String searchText;
    double latitude;
    double longitude;
}
