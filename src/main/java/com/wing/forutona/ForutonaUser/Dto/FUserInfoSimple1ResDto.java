package com.wing.forutona.ForutonaUser.Dto;

import lombok.Data;

@Data
public class FUserInfoSimple1ResDto {
    private String nickName;
    private double cumulativeInfluence;
    private long followCount;
    private String profilePictureUrl;
}
