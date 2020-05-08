package com.wing.forutona.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PhoneAuthReqDto {
    String phoneNumber;
    String internationalizedPhoneNumber;
    String isoCode;
}
