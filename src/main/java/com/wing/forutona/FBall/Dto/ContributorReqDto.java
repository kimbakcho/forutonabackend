package com.wing.forutona.FBall.Dto;

import lombok.Data;

@Data
public class ContributorReqDto {
    String uid;
    String ballUuid;

    public ContributorReqDto(String uid, String ballUuid) {
        this.uid = uid;
        this.ballUuid = ballUuid;
    }
}
