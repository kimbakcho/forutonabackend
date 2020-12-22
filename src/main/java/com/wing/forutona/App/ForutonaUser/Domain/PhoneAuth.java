package com.wing.forutona.App.ForutonaUser.Domain;

import com.wing.forutona.App.ForutonaUser.Dto.PhoneAuthReqDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
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



    public static PhoneAuth fromPhoneAuthReqDto (PhoneAuthReqDto reqDto){
        //TODO 서비스로 옮겨 준다.
        PhoneAuth phoneAuth = new PhoneAuth();
        phoneAuth.phoneNumber = reqDto.getPhoneNumber();
        phoneAuth.internationalizedPhoneNumber = reqDto.getInternationalizedPhoneNumber();
        double dValue = Math.random();
        int iValue = (int) (dValue * 100000) + 100000;
        phoneAuth.authNumber  = String.format("%d", iValue);
        phoneAuth.authTime = LocalDateTime.now().plusMinutes(30);
        phoneAuth.makeTime = LocalDateTime.now();
        phoneAuth.authRetryAvailableTime = LocalDateTime.now().plusMinutes(2);
        phoneAuth.isoCode = reqDto.getIsoCode();
        return phoneAuth;
    }

}
