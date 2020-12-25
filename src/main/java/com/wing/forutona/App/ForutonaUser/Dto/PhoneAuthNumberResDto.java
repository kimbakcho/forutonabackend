package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PhoneAuthNumberResDto {
    //Phone이 인증된 암호화 토큰
    String phoneAuthToken;
    String phoneNumber;
    String internationalizedDialCode;
    boolean errorFlag;
    String errorCause;
}
