package com.wing.forutona.FBall.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FBallReplyResWrapDto {
    int count;
    Long replyTotalCount;
    List<FBallReplyResDto> contents;
}
