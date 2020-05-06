package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.ForutonaUser.Dto.FUserSnSLoginReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.ForutonaUser.Dto.NaverGetMeResDto;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class NaverLoginService implements SnsLoginService {
    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(FUserSnSLoginReqDto reqDto) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer " + reqDto.getAccessToken());
        ResponseEntity<NaverGetMeResDto> response = new RestTemplate().exchange("https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET, new HttpEntity(header), NaverGetMeResDto.class);
        FUserSnsCheckJoinResDto fUserSnsGetMeResDto = new FUserSnsCheckJoinResDto();
        UserRecord recode;
        try {
            recode = FirebaseAuth.getInstance().getUser("Facebook"+response.getBody().getResponse().getId());
        } catch (Exception ex) {
            recode = null;
        }
        if(recode != null){
            fUserSnsGetMeResDto.setJoin(true);
            fUserSnsGetMeResDto.setSnsUid(String.valueOf(response.getBody().getResponse().getId()));
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
