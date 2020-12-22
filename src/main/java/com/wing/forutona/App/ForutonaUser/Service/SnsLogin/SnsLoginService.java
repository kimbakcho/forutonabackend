package com.wing.forutona.App.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinReqDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinResDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;

abstract public class SnsLoginService {

    private FUserInfoDataRepository fUserInfoDataRepository;

    public SnsLoginService(FUserInfoDataRepository fUserInfoDataRepository){
        this.fUserInfoDataRepository = fUserInfoDataRepository;
    }


    public abstract FUserSnsCheckJoinResDto getInfoFromToken(SnsSupportService snsService,String accessToken);

    public FUserInfoJoinResDto join(FUserInfoJoinReqDto reqDto) throws FirebaseAuthException {
        FUserSnsCheckJoinResDto infoFromToken = getInfoFromToken(reqDto.getSnsSupportService(),reqDto.getSnsToken());
        String fireBaseUid = reqDto.getSnsSupportService() + infoFromToken.getSnsUid();
        reqDto.setEmailUserUid(fireBaseUid);
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
