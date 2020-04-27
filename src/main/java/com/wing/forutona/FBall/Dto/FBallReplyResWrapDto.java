package com.wing.forutona.FBall.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FBallReplyResWrapDto {
    int count;
    List<FBallReplyResDto> contents;
}
