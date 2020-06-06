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
import com.wing.forutona.ForutonaUser.Service.SnsLogin.*;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.UUID;

@Service
public class FUserInfoService {
    @Autowired
    FUserInfoQueryRepository fUserInfoQueryRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    GoogleStorgeAdmin googleStorgeAdmin;

    /**
     * 닉네임 중복을 체크
     *
     * @param emitter
     * @param nickName
     */
    @Async
    @Transactional
    public void checkNickNameDuplication(ResponseBodyEmitter emitter, String nickName) {
        try {
            NickNameDuplicationCheckResDto resDto = new NickNameDuplicationCheckResDto();
            if (fUserInfoDataRepository.countByNickNameEquals(nickName) > 0) {
                resDto.setHaveNickName(true);
                emitter.send(resDto);
            } else {
                resDto.setHaveNickName(false);
                emitter.send(resDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    public void userPwChange(ResponseBodyEmitter emitter, FFireBaseToken fFireBaseToken, FUserInfoPwChangeReqDto reqDto) {
        UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(fFireBaseToken.getFireBaseToken().getUid());
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
    public void getMe(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getFireBaseToken().getUid()).get();
        try {
            emitter.send(new FUserInfoResDto(fUserInfo));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateUserProfileImage(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken, MultipartFile file) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getFireBaseToken().getUid()).get();
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
            FUserInfo fUserInfo = fUserInfoDataRepository.findById(fFireBaseToken.getFireBaseToken().getUid()).get();
            fUserInfo.setIsoCode(reqdto.getIsoCode());
            //이전 자신의 닉네임과 같지 않을때 중복 체크후 닉네임 설정
            if (!fUserInfo.getNickName().equals(reqdto.getNickName())) {
                if (fUserInfoDataRepository.countByNickNameEquals(reqdto.getNickName()) == 0) {
                    fUserInfo.setNickName(reqdto.getNickName());
                }
            }
            fUserInfo.setSelfIntroduction(reqdto.getSelfIntroduction());
            if(reqdto.getUserProfileImageUrl() != null){
                fUserInfo.setProfilePictureUrl(reqdto.getUserProfileImageUrl());
            }
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    /**
     * 클라이언트 기기에서 개인 정보 해킹을 막기위한 최소한의 정보만 주기 위한 메소드
     *
     * @param emitter
     * @param reqDto
     */
    @Async
    @Transactional
    public void getUserInfoSimple1(ResponseBodyEmitter emitter, FUserReqDto reqDto) {
        try {
            FUserInfo fUserInfo = fUserInfoDataRepository.findById(reqDto.getUid()).get();
            FUserInfoSimple1ResDto fUserInfoSimple1ResDto = new FUserInfoSimple1ResDto();
            fUserInfoSimple1ResDto.setNickName(fUserInfo.getNickName());
            fUserInfoSimple1ResDto.setCumulativeInfluence(fUserInfo.getCumulativeInfluence());
            fUserInfoSimple1ResDto.setFollowCount(fUserInfo.getFollowCount());
            fUserInfoSimple1ResDto.setProfilePictureUrl(fUserInfo.getProfilePictureUrl());
            emitter.send(fUserInfoSimple1ResDto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    /**
     * SNS 로그인 할때 회원 가입 여부 판단
     *
     * @param emitter
     * @param snSLoginReqDto
     */
    @Async
    @Transactional
    public void getSnsUserJoinCheckInfo(ResponseBodyEmitter emitter, FUserSnSLoginReqDto snSLoginReqDto) {

        try {
            SnsLoginService snsLoginService = null;
            if (snSLoginReqDto.getSnsService() == SnsSupportService.FaceBook) {
                snsLoginService = new FaceBookLoginService(fUserInfoDataRepository);
            } else if (snSLoginReqDto.getSnsService() == SnsSupportService.Kakao) {
                snsLoginService = new KakaoLoginService(fUserInfoDataRepository);
            } else if (snSLoginReqDto.getSnsService() == SnsSupportService.Naver) {
                snsLoginService = new NaverLoginService(fUserInfoDataRepository);
            } else {
                throw new Exception("dont'have service");
            }
            emitter.send(snsLoginService.getInfoFromToken(snSLoginReqDto));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void joinUser(ResponseBodyEmitter emitter, FUserInfoJoinReqDto reqDto) {
        try {
            SnsLoginService snsLoginService = null;
            if (reqDto.getSnsSupportService().equals(SnsSupportService.FaceBook)) {
                snsLoginService= new FaceBookLoginService(fUserInfoDataRepository);
            } else if (reqDto.getSnsSupportService().equals(SnsSupportService.Naver)) {
                snsLoginService= new NaverLoginService(fUserInfoDataRepository);
            } else if (reqDto.getSnsSupportService().equals(SnsSupportService.Kakao)) {
                snsLoginService= new KakaoLoginService(fUserInfoDataRepository);
            } else if(reqDto.getSnsSupportService().equals(SnsSupportService.Forutona)){
                snsLoginService= new ForutonaLoginService(fUserInfoDataRepository);
            } else {
                throw new Exception("Not Support SnsService");
            }
            FUserInfoJoinResDto join = snsLoginService.join(reqDto);
            emitter.send(join);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            emitter.complete();
        }

    }
}
