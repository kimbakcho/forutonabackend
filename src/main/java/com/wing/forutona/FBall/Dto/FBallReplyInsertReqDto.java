package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallReplyInsertReqDto {
    String replyUuid;
    String ballUuid;
    Long replyNumber;
    String replyText;
}
