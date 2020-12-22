package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PwFindPhoneAuthReqDto extends PhoneAuthReqDto {
    String email;
    String emailPhoneAuthToken;
}
