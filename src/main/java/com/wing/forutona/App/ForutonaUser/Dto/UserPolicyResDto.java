package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Domain.UserPolicy;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPolicyResDto {
    String policyName;
    String policyContent;
    String lang;
    LocalDateTime writeDateTime;

    public UserPolicyResDto(UserPolicy userPolicy){
        this.policyContent = userPolicy.getPolicyContent();
        this.policyName = userPolicy.getPolicyName();
        this.lang = userPolicy.getLang();
        this.writeDateTime = userPolicy.getWriteDateTime();
    }
}
