package com.wing.forutona.ForutonaUser.Dto;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
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
    Long followCount;

    public FUserInfoSimpleResDto(FUserInfo fUserInfo){
        this.uid = fUserInfo.getUid();
        this.nickName = fUserInfo.getNickName();
        this.profilePictureUrl = fUserInfo.getProfilePictureUrl();
        this.isoCode = fUserInfo.getIsoCode();
        this.userLevel = fUserInfo.getUserLevel();
        this.selfIntroduction = fUserInfo.getSelfIntroduction();
        this.cumulativeInfluence = fUserInfo.getCumulativeInfluence();
        this.followCount = fUserInfo.getFollowCount();
    }
}
