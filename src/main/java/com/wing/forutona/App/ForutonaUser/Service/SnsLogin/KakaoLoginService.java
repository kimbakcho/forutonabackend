package com.wing.forutona.App.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.App.ForutonaUser.Dto.*;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.App.ForutonaUser.Service.FUserInfoService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class KakaoLoginService extends SnsLoginService{
    public KakaoLoginService(FUserInfoDataRepository fUserInfoDataRepository, FUserInfoService fUserInfoService) {
        super(fUserInfoDataRepository, fUserInfoService);
    }

    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(SnsSupportService snsService, String accessToken) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        header.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        ResponseEntity<KakaoGetMeResDto> response = new RestTemplate().exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, new HttpEntity(header), KakaoGetMeResDto.class);
        FUserSnsCheckJoinResDto fUserSnsGetMeResDto = new FUserSnsCheckJoinResDto();
        UserRecord recode;
        try {
            recode = FirebaseAuth.getInstance().getUser(snsService.name()+response.getBody().getId());
        } catch (Exception ex) {
            recode = null;
        }
        if(recode != null){
            fUserSnsGetMeResDto.setJoin(true);
            fUserSnsGetMeResDto.setSnsUid(String.valueOf(response.getBody().getId()));
            String customToken = null;
            try {
                customToken = FirebaseAuth.getInstance().createCustomToken(recode.getUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
            fUserSnsGetMeResDto.setFirebaseCustomToken(customToken);
        }else {
            fUserSnsGetMeResDto.setJoin(false);
            fUserSnsGetMeResDto.setSnsUid(String.valueOf(response.getBody().getId()));
            fUserSnsGetMeResDto.setEmail(response.getBody().getKakao_account().getEmail());
            if(response.getBody().getKakao_account().getProfile().getProfile_image_url() != null){
                fUserSnsGetMeResDto.setPictureUrl(response.getBody().getKakao_account().getProfile().getProfile_image_url());
            }
            fUserSnsGetMeResDto.setUserSnsName(response.getBody().getKakao_account().getProfile().getNickname());
        }
        return fUserSnsGetMeResDto;
    }

}
