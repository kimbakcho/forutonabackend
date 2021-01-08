package com.wing.forutona.App.ForutonaUser.Service;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.*;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoQueryRepository;
import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsLoginService;
import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsLoginServiceFactory;
import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsSupportService;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FUserInfoService {
    FUserInfoResDto selectFUserInfo(String uid);

    void updateUserPosition(UserPositionUpdateReqDto reqDto, FUserInfo fUserInfo);

    void updateFireBaseMessageToken(String uid, String token);

    boolean checkNickNameDuplication(String nickName);

    void userPwChange(UserAdapter userAdapter, String pw) throws FirebaseAuthException;

    String updateUserProfileImage(FUserInfo fUserInfo, MultipartFile file) throws IOException;

    String updateUserBackGroundImage(FUserInfo fUserInfo ,MultipartFile file) throws IOException;

    FUserInfoResDto updateAccountUserInfo(UserAdapter userAdapter, FUserAccountUpdateReqDto reqDto,MultipartFile profileImage,MultipartFile backGroundImage) throws IOException;

    Page<FUserInfoSimpleResDto> getUserNickNameWithFullTextMatchIndex(String searchNickName, Pageable pageable);

    void updateMaliciousMessageCheck(FUserInfo getFUserInfo);

    FUserInfoSimpleResDto getFUserInfoSimple(String userUid);
}

@Service
@RequiredArgsConstructor
@Transactional
class FUserInfoServiceImpl implements FUserInfoService {

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FUserInfoQueryRepository fUserInfoQueryRepository;

    final GoogleStorgeAdmin googleStorgeAdmin;

    @Override
    public FUserInfoResDto selectFUserInfo(String uid) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();
        return new FUserInfoResDto(fUserInfo);
    }

    @Override
    public void updateUserPosition(UserPositionUpdateReqDto reqDto, FUserInfo fUserInfo ) {
        fUserInfo.updatePlacePoint(reqDto.getLat(), reqDto.getLng());
    }

    @Override
    public void updateFireBaseMessageToken(String uid, String token) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();
        fUserInfo.setFCMtoken(token);
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
    public void userPwChange(UserAdapter userAdapter, String pw) throws FirebaseAuthException {
        UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(userAdapter.getfUserInfo().getUid());
        updateRequest.setPassword(pw);
        FirebaseAuth.getInstance().updateUser(updateRequest);
    }

    @Override
    public String updateUserProfileImage(FUserInfo fUserInfo , MultipartFile file) throws IOException {
        String imageUrl = saveProfileStorageImage(file);
        fUserInfo.setProfilePictureUrl(imageUrl);
        return imageUrl;
    }

    @Override
    public String updateUserBackGroundImage(FUserInfo fUserInfo ,MultipartFile file) throws IOException {
        String imageUrl = saveProfileStorageImage(file);
        fUserInfo.setBackGroundImageUrl(imageUrl);
        return imageUrl;
    }

    public String saveProfileStorageImage(MultipartFile file) throws IOException {
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
        return imageUrl;
    }

    @Override
    public FUserInfoResDto updateAccountUserInfo(UserAdapter userAdapter,
                                                 FUserAccountUpdateReqDto reqDto,MultipartFile profileImage,MultipartFile backGroundImage) throws IOException {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();
        fUserInfo.setIsoCode(reqDto.getIsoCode());
        //이전 자신의 닉네임과 같지 않을때 중복 체크후 닉네임 설정
        if (!fUserInfo.getNickName().equals(reqDto.getNickName())) {
            if (fUserInfoDataRepository.countByNickNameEquals(reqDto.getNickName()) == 0) {
                fUserInfo.setNickName(reqDto.getNickName());
            }
        }
        fUserInfo.setSelfIntroduction(reqDto.getSelfIntroduction());
        if(profileImage != null){
            updateUserProfileImage(fUserInfo,profileImage);
        }
        if(backGroundImage != null){
            updateUserBackGroundImage(fUserInfo,backGroundImage);
        }

        if(reqDto.isProfileImageIsEmpty()){
            fUserInfo.setProfilePictureUrl(null);
        }
        if(reqDto.isBackGroundIsEmpty()){
            fUserInfo.setBackGroundImageUrl(null);
        }
        return new FUserInfoResDto(fUserInfo);
    }

    @Override
    public Page<FUserInfoSimpleResDto> getUserNickNameWithFullTextMatchIndex(String searchNickName, Pageable pageable) {
        return fUserInfoQueryRepository.findByUserNickNameWithFullTextMatchIndex(searchNickName,pageable);
    }

    @Override
    public void updateMaliciousMessageCheck(FUserInfo fUserInfo) {
        FUserInfo fUserInfo1 = fUserInfoDataRepository.findById(fUserInfo.getUid()).get();
        fUserInfo1.setMaliciousMessageCheck(true);
    }

    @Override
    public FUserInfoSimpleResDto getFUserInfoSimple(String userUid) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();
        return new FUserInfoSimpleResDto(fUserInfo);
    }


}
