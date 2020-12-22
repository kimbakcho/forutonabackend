package com.wing.forutona.App.ForutonaUser.Service.SnsLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnsLoginServiceFactory {

    final FaceBookLoginService faceBookLoginService;

    final NaverLoginService naverLoginService;

    final  KakaoLoginService kakaoLoginService;

    final ForutonaLoginService forutonaLoginService;

    public SnsLoginService makeService(SnsSupportService snsSupportService) throws Exception {
        if (snsSupportService.equals(SnsSupportService.FaceBook)) {
            return faceBookLoginService;
        } else if (snsSupportService.equals(SnsSupportService.Naver)) {
           return naverLoginService;
        } else if (snsSupportService.equals(SnsSupportService.Kakao)) {
            return  kakaoLoginService;
        } else if (snsSupportService.equals(SnsSupportService.Forutona)) {
            return forutonaLoginService;
        } else {
            throw new Exception("Not Support SnsService");
        }
    }
}
