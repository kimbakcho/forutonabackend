package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoJoinReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoJoinResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnSLoginReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

abstract public class SnsLoginService {

    private FUserInfoDataRepository fUserInfoDataRepository;

    public SnsLoginService(FUserInfoDataRepository fUserInfoDataRepository){
        this.fUserInfoDataRepository = fUserInfoDataRepository;
    }


    public abstract FUserSnsCheckJoinResDto getInfoFromToken(FUserSnSLoginReqDto reqDto);

    public FUserInfoJoinResDto join(FUserInfoJoinReqDto reqDto){

        FUserSnSLoginReqDto snSLoginReqDto = new FUserSnSLoginReqDto();
        snSLoginReqDto.setSnsService(reqDto.getSnsSupportService());
        snSLoginReqDto.setAccessToken(reqDto.getSnsToken());
        FUserSnsCheckJoinResDto infoFromToken = getInfoFromToken(snSLoginReqDto);
        String fireBaseuid = reqDto.getSnsSupportService() + infoFromToken.getSnsUid();
        reqDto.setEmailUserUid(fireBaseuid);
        FUserInfo fUserInfo = FUserInfo.fromFUserInfoJoinReqDto(reqDto);

        String customToken = null;
        try {
            customToken = FirebaseAuth.getInstance().createCustomToken(fUserInfo.getUid());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        fUserInfoDataRepository.save(fUserInfo);
        FUserInfoJoinResDto resDto = new FUserInfoJoinResDto();
        resDto.setCustomToken(customToken);
        resDto.setJoinComplete(true);
        return resDto;
    };
}
