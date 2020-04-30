package com.wing.forutona.FBall.Dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FBallValuationWrapResDto {
    int count;
    List<FBallValuationResDto>  contents = new ArrayList<>();

}
