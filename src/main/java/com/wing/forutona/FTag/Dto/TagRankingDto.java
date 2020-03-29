package com.wing.forutona.FTag.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class TagRankingDto {
    int ranking;
    String tagName;
    double tagPower;

    @QueryProjection
    public TagRankingDto(String tagName, double tagPower) {
        this.tagName = tagName;
        this.tagPower = tagPower;
    }

}
