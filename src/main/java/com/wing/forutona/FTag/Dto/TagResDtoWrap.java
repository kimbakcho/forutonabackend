package com.wing.forutona.FTag.Dto;

import lombok.Data;

import java.util.List;

@Data
public class TagResDtoWrap {
    int totalCount;
    List<TagResDto> tags;
}
