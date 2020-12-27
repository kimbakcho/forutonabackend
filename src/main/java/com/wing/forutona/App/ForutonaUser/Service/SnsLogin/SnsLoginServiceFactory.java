package com.wing.forutona.App.ForutonaUser.Service.SnsLogin;

import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.App.ForutonaUser.Service.FUserInfoService;
import com.wing.forutona.CustomUtil.FireBaseAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnsLoginServiceFactory {

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FUserInfoService fUserInfoService;

    final FireBaseAdmin fireBaseAdmin;

    public SnsLoginService makeService(SnsSupportService snsSupportService) throws Exception {
        if (snsSupportService.equals(SnsSupportService.FaceBook)) {
            return new FaceBookLoginService(fUserInfoDataRepository,fireBaseAdmin,fUserInfoService);
        } else if (snsSupportService.equals(SnsSupportService.Naver)) {
           return new NaverLoginService(fUserInfoDataRepository,fUserInfoService);
        } else if (snsSupportService.equals(SnsSupportService.Kakao)) {
            return  new KakaoLoginService(fUserInfoDataRepository,fUserInfoService);
        } else if (snsSupportService.equals(SnsSupportService.Forutona)) {
            return new ForutonaLoginService(fUserInfoDataRepository,fUserInfoService);
        } else {
            throw new Exception("Not Support SnsService");
        }
    }
}
