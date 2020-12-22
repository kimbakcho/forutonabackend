package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Domain.PhoneAuth;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PhoneAuthResDto {
    String phoneNumber;
    String internationalizedPhoneNumber;
    String isoCode;
    LocalDateTime authTime;
    LocalDateTime authRetryAvailableTime;
    LocalDateTime makeTime;
    public PhoneAuthResDto(PhoneAuth item){
        this.phoneNumber = item.getPhoneNumber();
        this.internationalizedPhoneNumber = item.getInternationalizedPhoneNumber();
        this.isoCode = item.getIsoCode();
        this.authTime = item.getAuthTime();
        this.authRetryAvailableTime = item.getAuthRetryAvailableTime();
        this.makeTime = item.getMakeTime();
    }
}
