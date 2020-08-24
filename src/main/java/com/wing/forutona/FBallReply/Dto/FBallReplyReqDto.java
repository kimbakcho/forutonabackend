package com.wing.forutona.FBallReply.Dto;

import lombok.Data;

@Data
public class FBallReplyReqDto {
    String ballUuid;
    Long replyNumber;
    boolean reqOnlySubReply;
}
