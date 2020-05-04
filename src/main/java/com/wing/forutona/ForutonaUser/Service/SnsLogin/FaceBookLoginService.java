package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.ForutonaUser.Dto.FUserSnSLoginReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.ForutonaUser.Dto.FaceBookGetMeResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FaceBookLoginService implements SnsLoginService {

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    public FaceBookLoginService() {
    }

    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(FUserSnSLoginReqDto reqDto) {
        String getUrl = "https://graph.facebook.com/v6.0/me?fields=name,first_name,last_name,email&access_token=" + reqDto.getAccessToken();
        ResponseEntity<FaceBookGetMeResDto> response = new RestTemplate().getForEntity(getUrl, FaceBookGetMeResDto.class);
        FUserSnsCheckJoinResDto fUserSnsGetMeResDto = new FUserSnsCheckJoinResDto();
        UserRecord recode;
        try {
            recode = FirebaseAuth.getInstance().getUser("Facebook"+response.getBody().getId());
        } catch (Exception ex) {
            recode = null;
        }
        if(recode != null){
            fUserSnsGetMeResDto.setJoin(true);
            fUserSnsGetMeResDto.setSnsUid(response.getBody().getId());
        }else {
            fUserSnsGetMeResDto.setJoin(false);
            fUserSnsGetMeResDto.setSnsUid(response.getBody().getId());
            fUserSnsGetMeResDto.setEmail(response.getBody().getEmail());
            if(response.getBody().getPicture() != null){
                fUserSnsGetMeResDto.setPictureUrl(response.getBody().getPicture().getData().getUrl());
            }
            fUserSnsGetMeResDto.setUserSnsName(response.getBody().getName());
        }
        return fUserSnsGetMeResDto;
    }

}