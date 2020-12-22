package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PwFindPhoneAuthNumberResDto extends PhoneAuthNumberResDto{
    String email;
    //emial + Phone이 인증된 암호화 토큰
    String emailPhoneAuthToken;
}
