package com.wing.forutona.ForutonaUser.Dto;

import lombok.Data;

@Data
public class PwChangeFromPhoneAuthResDto {
    String email;
    String internationalizedPhoneNumber;
    boolean errorFlag;
    String cause;
}
