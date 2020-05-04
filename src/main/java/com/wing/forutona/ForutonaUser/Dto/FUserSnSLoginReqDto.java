package com.wing.forutona.ForutonaUser.Dto;

import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsSupportService;
import lombok.Data;

@Data
public class FUserSnSLoginReqDto {
    String accessToken;
    String snsUid;
    SnsSupportService snsService;
    String fUserUid;
}
