package com.wing.forutona.App.ForutonaUser.Service.SnsLogin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinReqDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinResDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserSnsCheckJoinResDto;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.App.ForutonaUser.Service.FUserInfoService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

abstract public class SnsLoginService {

    private FUserInfoDataRepository fUserInfoDataRepository;

    final FUserInfoService fUserInfoService;

    public SnsLoginService(FUserInfoDataRepository fUserInfoDataRepository, FUserInfoService fUserInfoService){
        this.fUserInfoDataRepository = fUserInfoDataRepository;
        this.fUserInfoService = fUserInfoService;
    }


    public abstract FUserSnsCheckJoinResDto getInfoFromToken(SnsSupportService snsService,String accessToken);

    public FUserInfoJoinResDto join(FUserInfoJoinReqDto reqDto, MultipartFile profileImage, MultipartFile backGroundImage) throws FirebaseAuthException, IOException {
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
        if(profileImage != null){
            fUserInfoService.updateUserProfileImage(fUserInfo,profileImage);
        }else {
            if(reqDto.getProfileImageUrl() != null){
                fUserInfo.setProfilePictureUrl(reqDto.getProfileImageUrl());
            }
        }
        if(backGroundImage != null){
            fUserInfoService.updateUserBackGroundImage(fUserInfo,backGroundImage);
        }

        fUserInfo.setInfluenceTicket(1);
        fUserInfo.setMaxInfluenceTicket(1);
        fUserInfo.setInfluenceTicketReceiveTime(LocalDateTime.now());
        fUserInfo.setNextGiveInfluenceTicketTime(LocalDateTime.now().plusHours(1));

        fUserInfoDataRepository.save(fUserInfo);
        FUserInfoJoinResDto resDto = new FUserInfoJoinResDto();
        resDto.setCustomToken(customToken);
        resDto.setJoinComplete(true);
        return resDto;
    };
}
