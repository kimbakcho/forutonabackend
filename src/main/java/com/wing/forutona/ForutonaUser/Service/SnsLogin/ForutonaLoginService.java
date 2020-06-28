package com.wing.forutona.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.CustomUtil.SHA256Util;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoJoinReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoJoinResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnSLoginReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForutonaLoginService extends SnsLoginService {

    private FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    public ForutonaLoginService(FUserInfoDataRepository fUserInfoDataRepository) {
        super(fUserInfoDataRepository);
        this.fUserInfoDataRepository = fUserInfoDataRepository;
    }

    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(FUserSnSLoginReqDto reqDto) {
        return null;
    }

    @Override
    public FUserInfoJoinResDto join(FUserInfoJoinReqDto reqDto) {
        FUserInfo fUserInfo = FUserInfo.fromFUserInfoJoinReqDto(reqDto);
        String encSHA256 = "";
        FUserInfoJoinResDto resDto = new FUserInfoJoinResDto();
        try {
            encSHA256 = SHA256Util.getEncSHA256(reqDto.getInternationalizedPhoneNumber() + "Forutona123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(encSHA256.equals(reqDto.getPhoneAuthToken())){
            UserRecord user = null;
            try {
                user = FirebaseAuth.getInstance().getUser(reqDto.getEmailUserUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
            if(user == null){
                resDto.setCustomToken("");
                resDto.setJoinComplete(false);
            }else {
                fUserInfoDataRepository.save(fUserInfo);
                resDto.setCustomToken("");
                resDto.setJoinComplete(true);
            }
        }else {
            resDto.setCustomToken("");
            resDto.setJoinComplete(false);
        }
        return resDto;
    }

}
