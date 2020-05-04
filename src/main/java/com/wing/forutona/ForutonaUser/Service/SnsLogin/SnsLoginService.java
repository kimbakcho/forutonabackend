package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.wing.forutona.ForutonaUser.Dto.FUserSnSLoginReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnsCheckJoinResDto;

public interface SnsLoginService {
    FUserSnsCheckJoinResDto getInfoFromToken(FUserSnSLoginReqDto reqDto);
}
