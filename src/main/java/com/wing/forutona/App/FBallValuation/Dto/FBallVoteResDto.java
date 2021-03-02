package com.wing.forutona.App.FBallValuation.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FBallVoteResDto {
    Integer ballLike;
    Integer ballDislike;
    Integer ballPower;
    List<FBallValuationResDto> fballValuationResDtos;
}
