package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PhoneAuthReqDto {
    String phoneNumber;
    String internationalizedDialCode;
    String isoCode;
}
