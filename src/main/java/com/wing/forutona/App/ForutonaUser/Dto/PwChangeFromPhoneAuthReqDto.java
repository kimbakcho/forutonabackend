package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PwChangeFromPhoneAuthReqDto {
    String password;
    String email;
    String internationalizedPhoneNumber;
    String emailPhoneAuthToken;
}
