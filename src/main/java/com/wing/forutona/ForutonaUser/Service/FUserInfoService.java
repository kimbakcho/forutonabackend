package com.wing.forutona.ForutonaUser.Service;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsLoginService;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsLoginServiceFactory;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsSupportService;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FUserInfoService {
    FUserInfoResDto selectFUserInfo(String uid);

    void updateUserPosition(UserPositionUpdateReqDto reqDto, FFireBaseToken fireBaseToken);

    void updateFireBaseMessageToken(String uid, String token);

    FUserInfoJoinResDto joinUser(FUserInfoJoinReqDto reqDto) throws Exception;

    FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(SnsSupportService snsService, String accessToken) throws Exception;

    boolean checkNickNameDuplication(String nickName);

    void userPwChange(FFireBaseToken fFireBaseToken, String pw);

    String updateUserProfileImage(FFireBaseToken fireBaseToken, MultipartFile file) throws IOException;

    FUserInfoResDto updateAccountUserInfo(FFireBaseToken fFireBaseToken, FUserAccountUpdateReqDto reqDto);
}

@Service
@RequiredArgsConstructor
@Transactional
class FUserInfoServiceImpl implements FUserInfoService {

    final FUserInfoDataRepository fUserInfoDataRepository;

    final SnsLoginServiceFactory snsLoginServiceFactory;

    final GoogleStorgeAdmin googleStorgeAdmin;

    @Override
    public FUserInfoResDto selectFUserInfo(String uid) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();
        return new FUserInfoResDto(fUserInfo);
    }

    @Override
    public void updateUserPosition(UserPositionUpdateReqDto reqDto, FFireBaseToken fireBaseToken) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();
        fUserInfo.updatePlacePoint(reqDto.getLat(), reqDto.getLng());
    }

    @Override
    public void updateFireBaseMessageToken(String uid, String token) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();
        fUserInfo.setFCMtoken(token);
    }

    @Override
    public FUserInfoJoinResDto joinUser(FUserInfoJoinReqDto reqDto) throws Exception {
        SnsLoginService snsLoginService = snsLoginServiceFactory.makeService(reqDto.getSnsSupportService());
        return snsLoginService.join(reqDto);
    }

    @Override
    public FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(SnsSupportService snsService, String accessToken) throws Exception {
        SnsLoginService snsLoginService = snsLoginServiceFactory.makeService(snsService);
        return snsLoginService.getInfoFromToken(snsService, accessToken);
    }

    @Override
    public boolean checkNickNameDuplication(String nickName) {
        if (fUserInfoDataRepository.countByNickNameEquals(nickName) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void userPwChange(FFireBaseToken fFireBaseToken, String pw) {
        UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(fFireBaseToken.getUserFireBaseUid());
        updateRequest.setPassword(pw);
    }

    @Override
    public String updateUserProfileImage(FFireBaseToken fireBaseToken, MultipartFile file) throws IOException {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();
        Storage storage = googleStorgeAdmin.GetStorageInstance();
        String OriginalFile = file.getOriginalFilename();
        int extentIndex = OriginalFile.lastIndexOf(".");
        UUID uuid = UUID.randomUUID();
        String saveFileName = "";
        if (extentIndex > 0) {
            String extent = OriginalFile.substring(extentIndex);
            saveFileName = uuid.toString() + extent;
        } else {
            saveFileName = uuid.toString();
        }
        BlobId blobId = BlobId.of("publicforutona", "profileimage/" + saveFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        String imageUrl = "https://storage.googleapis.com/publicforutona/profileimage/" + saveFileName;
        fUserInfo.setProfilePictureUrl(imageUrl);
        return imageUrl;
    }

    @Override
    public FUserInfoResDto updateAccountUserInfo(FFireBaseToken fFireBaseToken, FUserAccountUpdateReqDto reqDto) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fFireBaseToken.getUserFireBaseUid()).get();
        fUserInfo.setIsoCode(reqDto.getIsoCode());
        //이전 자신의 닉네임과 같지 않을때 중복 체크후 닉네임 설정
        if (!fUserInfo.getNickName().equals(reqDto.getNickName())) {
            if (fUserInfoDataRepository.countByNickNameEquals(reqDto.getNickName()) == 0) {
                fUserInfo.setNickName(reqDto.getNickName());
            }
        }
        fUserInfo.setSelfIntroduction(reqDto.getSelfIntroduction());
        if (reqDto.getUserProfileImageUrl() != null) {
            fUserInfo.setProfilePictureUrl(reqDto.getUserProfileImageUrl());
        }
        return new FUserInfoResDto(fUserInfo);
    }


}
