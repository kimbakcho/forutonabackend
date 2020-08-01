package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.CustomUtil.FireBaseAdmin;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FaceBookLoginService extends SnsLoginService {

    final FireBaseAdmin fireBaseAdmin;

    public FaceBookLoginService(FUserInfoDataRepository fUserInfoDataRepository,FireBaseAdmin fireBaseAdmin) {
        super(fUserInfoDataRepository);
        this.fireBaseAdmin = fireBaseAdmin;
    }

    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(SnsSupportService snsService, String accessToken) {
        String getUrl = "https://graph.facebook.com/v6.0/me?fields=name,first_name,last_name,email&access_token=" + accessToken;
        ResponseEntity<FaceBookGetMeResDto> response = new RestTemplate().getForEntity(getUrl, FaceBookGetMeResDto.class);
        FUserSnsCheckJoinResDto fUserSnsGetMeResDto = new FUserSnsCheckJoinResDto();
        UserRecord recode;
        try {
            recode = FirebaseAuth.getInstance().getUser(snsService.name()+response.getBody().getId());
        } catch (Exception ex) {
            recode = null;
        }
        if(recode != null){
            fUserSnsGetMeResDto.setJoin(true);
            fUserSnsGetMeResDto.setSnsUid(response.getBody().getId());
            String customToken = null;
            try {
                customToken = FirebaseAuth.getInstance().createCustomToken(recode.getUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
            fUserSnsGetMeResDto.setFirebaseCustomToken(customToken);
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
