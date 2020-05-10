package com.wing.forutona.ForutonaUser.Domain;

import com.wing.forutona.ForutonaUser.Dto.PhoneAuthReqDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuth {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;
    @Column(unique = true)
    String phoneNumber;
    String internationalizedPhoneNumber;
    String authNumber;
    LocalDateTime authTime;
    LocalDateTime makeTime;
    LocalDateTime authRetryAvailableTime;
    String isoCode;
    public PhoneAuth(PhoneAuthReqDto reqDto){
        this.phoneNumber = reqDto.getPhoneNumber();
        this.internationalizedPhoneNumber = reqDto.getInternationalizedPhoneNumber();
        double dValue = Math.random();
        int iValue = (int) (dValue * 100000) + 100000;
        this.authNumber  = String.format("%d", iValue);
        this.authTime = LocalDateTime.now().plusMinutes(30);
        this.makeTime = LocalDateTime.now();
        this.authRetryAvailableTime = LocalDateTime.now().plusMinutes(2);
        this.isoCode = reqDto.getIsoCode();
    }

}
