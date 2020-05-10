package com.wing.forutona.ForutonaUser.Dto;

import com.wing.forutona.ForutonaUser.Domain.PhoneAuth;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PwFindPhoneAuthResDto extends PhoneAuthResDto  {
    public PwFindPhoneAuthResDto(PhoneAuth item){
        super(item);
    }
    boolean error;
    String cause;
    String email;
}
