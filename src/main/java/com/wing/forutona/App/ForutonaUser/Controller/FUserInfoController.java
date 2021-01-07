package com.wing.forutona.App.ForutonaUser.Controller;

import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsLoginService;
import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsLoginServiceFactory;
import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.App.ForutonaUser.Dto.*;
import com.wing.forutona.App.ForutonaUser.Service.FUserInfoService;
import com.wing.forutona.App.ForutonaUser.Service.SnsLogin.SnsSupportService;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class FUserInfoController {

    final FUserInfoService fUserInfoService;

    final SnsLoginServiceFactory snsLoginServiceFactory;

    @GetMapping(value = "/v1/FUserInfo")
    FUserInfoResDto selectFUserInfo(@AuthenticationPrincipal UserAdapter userAdapter) throws Exception {
        return fUserInfoService.selectFUserInfo(userAdapter.getfUserInfo().getUid());
    }

    @PutMapping(value = "/v1/FUserInfo/UserPosition")
    void updateUserPosition(UserPositionUpdateReqDto reqDto, @AuthenticationPrincipal UserAdapter userAdapter) throws Exception {
        fUserInfoService.updateUserPosition(reqDto,userAdapter.getfUserInfo());
    }

    @PutMapping(value = "/v1/FUserInfo/FireBaseMessageToken")
    public void updateFireBaseMessageToken(@RequestParam String token,@AuthenticationPrincipal UserAdapter userAdapter) {
        fUserInfoService.updateFireBaseMessageToken(userAdapter.getfUserInfo().getUid(), token);
    }


    @PostMapping(value = "/v1/FUserInfo/JoinUser")
    @Transactional
    public FUserInfoJoinResDto joinUser(FUserInfoJoinReqDto reqDto,MultipartFile profileImage,MultipartFile backGroundImage) throws Exception {
        SnsLoginService snsLoginService = snsLoginServiceFactory.makeService(reqDto.getSnsSupportService());
        return snsLoginService.join(reqDto,profileImage,backGroundImage);
    }

    @ResponseAddJsonHeader
    @Transactional
    @GetMapping(value = "/v1/FUserInfo/SnsUserJoinCheckInfo")
    public FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(SnsSupportService snsService, String accessToken) throws Exception {
        SnsLoginService snsLoginService = snsLoginServiceFactory.makeService(snsService);
        return snsLoginService.getInfoFromToken(snsService, accessToken);
    }


    @GetMapping(value = "/v1/FUserInfo/CheckNickNameDuplication")
    public boolean checkNickNameDuplication(@RequestParam String nickName) {
        return fUserInfoService.checkNickNameDuplication(nickName);
    }


    @PutMapping(value = "/v1/FUserInfo/AccountUserInfo")
    public FUserInfoResDto updateAccountUserInfo(
            @AuthenticationPrincipal UserAdapter userAdapter,
            FUserAccountUpdateReqDto reqDto,MultipartFile profileImage,MultipartFile backGroundImage) throws IOException {
        return fUserInfoService.updateAccountUserInfo(userAdapter,reqDto,profileImage,backGroundImage);
    }

//    @AuthFireBaseJwtCheck
//    @ResponseAddJsonHeader
//    @PutMapping(value = "/v1/FUserInfo/ProfileImage")
//    public ResponseBodyEmitter updateUserProfileImage(FFireBaseToken fFireBaseToken,
//                                                      @RequestParam("ProfileImage") MultipartFile file) {
//        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(() -> {
//            try {
//                emitter.send(fUserInfoService.updateUserProfileImage(fFireBaseToken, file));
//                emitter.complete();
//            } catch (IOException e) {
//                e.printStackTrace();
//                emitter.completeWithError(e);
//            }
//        });
//        return emitter;
//    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FUserInfo/PwChange")
    public void userPwChange(FFireBaseToken fFireBaseToken, String pw) {
        fUserInfoService.userPwChange(fFireBaseToken, pw);
    }

    @GetMapping(value = "/v1/FUserInfo/UserNickNameWithFullTextMatchIndex")
    public Page<FUserInfoSimpleResDto> getUserNickNameWithFullTextMatchIndex(@RequestParam String searchNickName, Pageable pageable) {
        return fUserInfoService.getUserNickNameWithFullTextMatchIndex(searchNickName, pageable);
    }


    @PutMapping(value = "/v1/FUserInfo/maliciousMessageCheck")
    public void updateMaliciousMessageCheck(@AuthenticationPrincipal UserAdapter userAdapter) {
        fUserInfoService.updateMaliciousMessageCheck(userAdapter.getfUserInfo());
    }

    @GetMapping(value= "/v1/FUserInfo/FUserInfoSimple")
    public FUserInfoSimpleResDto getFUserInfoSimple(String userUid){
        return fUserInfoService.getFUserInfoSimple(userUid);
    }

}
