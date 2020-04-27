package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallReplyInsertReqDto {
    String ballUuid;
    Long replyNumber;
    Long replySort;
    Long replyDepth;
    String replyText;
}
