package com.wing.forutona.FTag.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRankingDto {
    int ranking;
    String tagName;
    double tagPower;
    Long tagBallPower;

    @QueryProjection
    public TagRankingDto(String tagName, double tagPower) {
        this.tagName = tagName;
        this.tagPower = tagPower;
    }

}
