package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PhoneAuthNumberReqDto {
    String phoneNumber;
    String internationalizedDialCode;
    String isoCode;
    String authNumber;
}
