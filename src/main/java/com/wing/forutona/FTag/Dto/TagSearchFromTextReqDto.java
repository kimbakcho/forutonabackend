package com.wing.forutona.FTag.Dto;

import lombok.Data;

@Data
public class TagSearchFromTextReqDto {
    String searchText;
    double latitude;
    double longitude;
}
