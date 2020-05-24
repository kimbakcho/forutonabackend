package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class FBallReplyReqDto {
    String replyUuid;
    String ballUuid;
    Long replyNumber;
    boolean detail;
}
