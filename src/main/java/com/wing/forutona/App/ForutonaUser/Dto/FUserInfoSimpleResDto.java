package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FUserInfoSimpleResDto  implements Cloneable{
    String uid;
    String nickName;
    String profilePictureUrl;
    String backGroundImageUrl;
    String isoCode;
    Double userLevel;
    String selfIntroduction;
    Double cumulativeInfluence;
    Long followerCount;
    Long followingCount;
    Double playerPower;

    public FUserInfoSimpleResDto(FUserInfo fUserInfo){
        this.uid = fUserInfo.getUid();
        this.nickName = fUserInfo.getNickName();
        this.profilePictureUrl = fUserInfo.getProfilePictureUrl();
        this.backGroundImageUrl = fUserInfo.getBackGroundImageUrl();
        this.isoCode = fUserInfo.getIsoCode();
        this.userLevel = fUserInfo.getUserLevel();
        this.selfIntroduction = fUserInfo.getSelfIntroduction();
        this.cumulativeInfluence = fUserInfo.getCumulativeInfluence();
        this.followerCount = fUserInfo.getFollowerCount();
        this.followingCount = fUserInfo.getFollowingCount();
        this.playerPower = fUserInfo.getPlayerPower();
    }

    public Object clone() throws CloneNotSupportedException {
        FUserInfoSimpleResDto clone = (FUserInfoSimpleResDto)super.clone();
        return clone;
    }


}
