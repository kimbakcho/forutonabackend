package com.wing.forutona.FTag.Dto;

import lombok.Data;

@Data
public class TextMatchTagBallReqDto {
    String searchText;
    double mapCenterLatitude;
    double mapCenterLongitude;
}
