package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PhoneAuthNumberReqDto {
    String phoneNumber;
    String internationalizedPhoneNumber;
    String isoCode;
    String authNumber;
}
