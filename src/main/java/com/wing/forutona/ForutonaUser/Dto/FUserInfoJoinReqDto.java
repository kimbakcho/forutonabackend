package com.wing.forutona.ForutonaUser.Dto;

import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsSupportService;
import lombok.Data;

@Data
public class FUserInfoJoinReqDto {
    boolean forutonaAgree;
    boolean forutonaManagementAgree;
    boolean privateAgree;
    boolean positionAgree;
    boolean martketingAgree;
    boolean ageLimitAgree;
    String nickName;
    String email;
    String userProfileImageUrl;
    SnsSupportService snsSupportService;
    String countryCode;
    String snsToken;
    String userIntroduce;
}
