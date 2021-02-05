package com.wing.forutona.Manager.MaliciousReply.Dto;

import lombok.Data;

@Data
public class MaliciousReplyReqDto {
    private String replyUuid;
    private Integer crime;
    private Integer abuse;
    private Integer privacy;
    private Integer sexual;
    private Integer advertising;
    private Integer etc;
}
