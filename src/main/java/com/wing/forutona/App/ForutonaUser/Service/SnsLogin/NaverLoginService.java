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

public class NaverLoginService extends SnsLoginService {

    public NaverLoginService(FUserInfoDataRepository fUserInfoDataRepository, FUserInfoService fUserInfoService) {
        super(fUserInfoDataRepository, fUserInfoService);
    }

    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(SnsSupportService snsService, String accessToken) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        ResponseEntity<NaverGetMeResDto> response = new RestTemplate().exchange("https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET, new HttpEntity(header), NaverGetMeResDto.class);
        FUserSnsCheckJoinResDto fUserSnsGetMeResDto = new FUserSnsCheckJoinResDto();
        UserRecord recode;
        try {
            recode = FirebaseAuth.getInstance().getUser(snsService.name()+response.getBody().getResponse().getId());
        } catch (Exception ex) {
            recode = null;
        }
        if(recode != null){
            fUserSnsGetMeResDto.setJoin(true);
            fUserSnsGetMeResDto.setSnsUid(String.valueOf(response.getBody().getResponse().getId()));
            String customToken = null;
            try {
                customToken = FirebaseAuth.getInstance().createCustomToken(recode.getUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
            fUserSnsGetMeResDto.setFirebaseCustomToken(customToken);
        }else {
            fUserSnsGetMeResDto.setJoin(false);
            fUserSnsGetMeResDto.setSnsUid(String.valueOf(response.getBody().getResponse().getId()));
            fUserSnsGetMeResDto.setEmail(response.getBody().getResponse().getEmail());
            if(response.getBody().getResponse().getProfile_image() != null){
                fUserSnsGetMeResDto.setPictureUrl(response.getBody().getResponse().getProfile_image());
            }
            fUserSnsGetMeResDto.setUserSnsName(response.getBody().getResponse().getNickname());
        }
        return fUserSnsGetMeResDto;
    }


}
