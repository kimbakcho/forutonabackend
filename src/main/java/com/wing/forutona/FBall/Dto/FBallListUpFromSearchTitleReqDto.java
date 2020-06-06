package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallListUpFromSearchTitleReqDto {
    String searchText;
    final double latitude;
    final double longitude;
}
