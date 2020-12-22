package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Domain.PhoneAuth;
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
