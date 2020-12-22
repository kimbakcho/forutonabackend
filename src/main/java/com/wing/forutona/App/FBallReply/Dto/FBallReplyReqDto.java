package com.wing.forutona.App.FBallReply.Dto;

import lombok.Data;

@Data
public class FBallReplyReqDto {
    String ballUuid;
    Long replyNumber;
    boolean reqOnlySubReply;
}
