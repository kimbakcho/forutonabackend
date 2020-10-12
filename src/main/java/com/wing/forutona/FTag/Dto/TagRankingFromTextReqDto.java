package com.wing.forutona.FTag.Dto;

import lombok.Data;

@Data
public class TagRankingFromTextReqDto {
    double mapCenterLatitude;
    double mapCenterLongitude;
    String searchTagText;
}
