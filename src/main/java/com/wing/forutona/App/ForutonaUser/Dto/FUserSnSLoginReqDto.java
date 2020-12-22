package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsSupportService;
import lombok.Data;

@Data
public class FUserSnSLoginReqDto {
    String accessToken;
    String snsUid;
    SnsSupportService snsService;
    String fUserUid;
    String email;
}
