package com.wing.forutona.ForutonaUser.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoQueryRepository;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsLoginService;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsLoginServiceFactory;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.UUID;

public interface FAccountService {

    boolean checkNickNameDuplication(String nickName);

    void userPwChange(ResponseBodyEmitter emitter, FFireBaseToken fFireBaseToken, FUserInfoPwChangeReqDto reqDto);

    void updateUserProfileImage(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken, MultipartFile file);

    void updateAccountUserInfo(ResponseBodyEmitter emitter, FFireBaseToken fFireBaseToken, FuserAccountUpdateReqdto reqdto);


}

@Service
@RequiredArgsConstructor
class FAccountServiceImpl implements FAccountService {

    final FUserInfoQueryRepository fUserInfoQueryRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final GoogleStorgeAdmin googleStorgeAdmin;

    final SnsLoginServiceFactory snsLoginServiceFactory;

    @Transactional
    public boolean checkNickNameDuplication(String nickName) {
        if (fUserInfoDataRepository.countByNickNameEquals(nickName) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Async
    @Transactional
    public void userPwChange(ResponseBodyEmitter emitter, FFireBaseToken fFireBaseToken, FUserInfoPwChangeReqDto reqDto) {
        UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(fFireBaseToken.getUserFireBaseUid());
        updateRequest.setPassword(reqDto.getPw());
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(updateRequest);
            emitter.send(1);
        } catch (FirebaseAuthException | IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateUserProfileImage(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken, MultipartFile file) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();
        Storage storage = googleStorgeAdmin.GetStorageInstance();
        try {
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
            emitter.send(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateAccountUserInfo(ResponseBodyEmitter emitter, FFireBaseToken fFireBaseToken, FuserAccountUpdateReqdto reqdto) {
        try {
            FUserInfo fUserInfo = fUserInfoDataRepository.findById(fFireBaseToken.getUserFireBaseUid()).get();
            fUserInfo.setIsoCode(reqdto.getIsoCode());
            //이전 자신의 닉네임과 같지 않을때 중복 체크후 닉네임 설정
            if (!fUserInfo.getNickName().equals(reqdto.getNickName())) {
                if (fUserInfoDataRepository.countByNickNameEquals(reqdto.getNickName()) == 0) {
                    fUserInfo.setNickName(reqdto.getNickName());
                }
            }
            fUserInfo.setSelfIntroduction(reqdto.getSelfIntroduction());
            if (reqdto.getUserProfileImageUrl() != null) {
                fUserInfo.setProfilePictureUrl(reqdto.getUserProfileImageUrl());
            }
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }





}