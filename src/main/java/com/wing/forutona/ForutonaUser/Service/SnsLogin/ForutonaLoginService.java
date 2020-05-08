package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.wing.forutona.ForutonaUser.Dto.FUserSnSLoginReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnsCheckJoinResDto;

public class ForutonaLoginService implements SnsLoginService {

    //여기서는 다른 SNS 와 같이 인증 매체가 핸드폰이기 때문에 핸드폰 인증 토큰을 목적으로 한다.
    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(FUserSnSLoginReqDto reqDto) {

        return null;
    }

}
