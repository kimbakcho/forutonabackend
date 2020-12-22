package com.wing.forutona.Manager.MUserInfo.Dto;

import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import lombok.Data;

@Data
public class MUserInfoResDto {
    String uid;
    String userUuid;
    String userName;
    String groupName;
    String hasRole;

    public static MUserInfoResDto fromUserInfoResDto(MUserInfo userInfo) {
        MUserInfoResDto mUserInfoResDto = new MUserInfoResDto();
        mUserInfoResDto.uid = userInfo.getUid();
        mUserInfoResDto.userUuid = userInfo.getUserUuid();
        mUserInfoResDto.userName = userInfo.getUserName();
        mUserInfoResDto.groupName = userInfo.getGroupName();
        mUserInfoResDto.hasRole = userInfo.getHasRole();
        return mUserInfoResDto;
    }
}
