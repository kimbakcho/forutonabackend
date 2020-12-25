package com.wing.forutona.App.FTag.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class TagRankingResDto {
    String tagName;
    double tagPower;

    @QueryProjection
    public TagRankingResDto(String tagName, double tagPower) {
        this.tagName = tagName;
        this.tagPower = tagPower;
    }

}
