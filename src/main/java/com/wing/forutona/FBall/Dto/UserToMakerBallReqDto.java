package com.wing.forutona.FBall.Dto;

import com.wing.forutona.CustomUtil.MultiSort;
import lombok.Data;

import java.util.List;

@Data
public class UserToMakerBallReqDto {
    String makerUid;
    List<MultiSort> sorts;
}
