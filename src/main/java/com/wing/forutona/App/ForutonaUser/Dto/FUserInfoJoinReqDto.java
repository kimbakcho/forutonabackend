package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Domain.GenderType;
import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsSupportService;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    SnsSupportService snsSupportService;
    String countryCode;
    String snsToken;
    String userIntroduce;
    String internationalizedPhoneNumber;
    //해당 토큰으로 최종 가입 절차에서 인증 받은 폰인지 체크한다.
    String phoneAuthToken;
    String password;
    String emailUserUid;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime ageDate;
    GenderType gender;
}
