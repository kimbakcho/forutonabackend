package com.wing.forutona.App.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.App.ForutonaUser.Service.FUserInfoService;
import com.wing.forutona.CustomUtil.SHA256Util;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinReqDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinResDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ForutonaLoginService extends SnsLoginService {

    private FUserInfoDataRepository fUserInfoDataRepository;

    final FUserInfoService fUserInfoService;

    public ForutonaLoginService(FUserInfoDataRepository fUserInfoDataRepository, FUserInfoService fUserInfoService) {
        super(fUserInfoDataRepository, fUserInfoService);
        this.fUserInfoDataRepository = fUserInfoDataRepository;
        this.fUserInfoService = fUserInfoService;
    }

    @Override
    public FUserSnsCheckJoinResDto getInfoFromToken(SnsSupportService snsService,String accessToken) {
        return null;
    }

    @Override
    public FUserInfoJoinResDto join(FUserInfoJoinReqDto reqDto, MultipartFile profileImage, MultipartFile backGroundImage) throws FirebaseAuthException, IOException {
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
                FUserInfo saveUser = fUserInfoDataRepository.save(fUserInfo);
                if(profileImage != null){
                    fUserInfoService.updateUserProfileImage(saveUser,profileImage);
                }
                if(backGroundImage != null){
                    fUserInfoService.updateUserBackGroundImage(saveUser,backGroundImage);
                }
                user = FirebaseAuth.getInstance().getUser(reqDto.getEmailUserUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
            if(user == null){
                resDto.setCustomToken("");
                resDto.setJoinComplete(false);
            }else {
                String customToken = FirebaseAuth.getInstance().createCustomToken(reqDto.getEmailUserUid());
                resDto.setCustomToken(customToken);
                resDto.setJoinComplete(true);
            }
        }else {
            FirebaseAuth.getInstance().deleteUser(reqDto.getEmailUserUid());
            resDto.setCustomToken("");
            resDto.setJoinComplete(false);
        }
        return resDto;
    }

}
