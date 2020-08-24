package com.wing.forutona.FBallReply.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FBallReplyResWrapDto {
    int count;
    long offset;
    long pageSize;
    boolean onlySubReply;
    Long replyTotalCount;
    List<FBallReplyResDto> contents;
}
