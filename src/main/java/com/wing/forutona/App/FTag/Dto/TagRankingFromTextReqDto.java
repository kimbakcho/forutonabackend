package com.wing.forutona.App.FTag.Dto;

import lombok.Data;

@Data
public class TagRankingFromTextReqDto {
    double mapCenterLatitude;
    double mapCenterLongitude;
    String searchTagText;
}
