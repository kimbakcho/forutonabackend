package com.wing.forutona.ForutonaUser.Dto;

import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import lombok.Data;

@Data
public class FUserInfoSimpleResDto {
    String uid;
    String nickName;
    String profilePictureUrl;
    String isoCode;
    Double userLevel;
    String selfIntroduction;
    Double cumulativeInfluence;

    public FUserInfoSimpleResDto(FUserInfoSimple fUserInfoSimple){
        this.uid = fUserInfoSimple.getUid();
        this.nickName = fUserInfoSimple.getNickName();
        this.profilePictureUrl = fUserInfoSimple.getProfilePictureUrl();
        this.isoCode = fUserInfoSimple.getIsoCode();
        this.userLevel = fUserInfoSimple.getUserLevel();
        this.selfIntroduction = fUserInfoSimple.getSelfIntroduction();
        this.cumulativeInfluence = fUserInfoSimple.getCumulativeInfluence();
    }
}
