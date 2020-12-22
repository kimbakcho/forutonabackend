package com.wing.forutona.App.FTag.Dto;

import lombok.Data;

@Data
public class TextMatchTagBallReqDto {
    String searchText;
    double mapCenterLatitude;
    double mapCenterLongitude;
}
