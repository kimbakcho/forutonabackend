package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Service.FUserInfoService;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.SnsSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class FUserInfoController {

    final FUserInfoService fUserInfoService;

    @GetMapping(value = "/v1/FUserInfo")
    @AuthFireBaseJwtCheck
    FUserInfoResDto selectFUserInfo(FFireBaseToken fFireBaseToken) throws Exception {
        return fUserInfoService.selectFUserInfo(fFireBaseToken.getUserFireBaseUid());
    }

    @PutMapping(value = "/v1/FUserInfo/UserPosition")
    @AuthFireBaseJwtCheck
    void updateUserPosition(UserPositionUpdateReqDto reqDto,FFireBaseToken fFireBaseToken) throws Exception {
        fUserInfoService.updateUserPosition(reqDto, fFireBaseToken);
    }

    @PutMapping(value = "/v1/FUserInfo/FireBaseMessageToken")
    public void updateFireBaseMessageToken(@RequestParam String token,FFireBaseToken fFireBaseToken){
        fUserInfoService.updateFireBaseMessageToken(fFireBaseToken.getUserFireBaseUid(), token);
    }


    @PostMapping(value = "/v1/FUserInfo/JoinUser")
    public FUserInfoJoinResDto joinUser(@RequestBody FUserInfoJoinReqDto reqDto) throws Exception {
        return fUserInfoService.joinUser(reqDto);
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FUserInfo/SnsUserJoinCheckInfo")
    public FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(SnsSupportService snsService, String accessToken) throws Exception {
        return fUserInfoService.getSnsUserJoinCheckInfo(snsService,accessToken);
    }


    @GetMapping(value = "/v1/FUserInfo/CheckNickNameDuplication")
    public boolean checkNickNameDuplication(@RequestParam  String nickName){
        return fUserInfoService.checkNickNameDuplication(nickName);
    }


    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FUserInfo/AccountUserInfo")
    public FUserInfoResDto updateAccountUserInfo(FFireBaseToken fFireBaseToken, @RequestBody FUserAccountUpdateReqDto reqDto){
        return fUserInfoService.updateAccountUserInfo(fFireBaseToken,reqDto);
    }


    @AuthFireBaseJwtCheck
    @ResponseAddJsonHeader
    @PutMapping(value = "/v1/FUserInfo/ProfileImage")
    public ResponseBodyEmitter updateUserProfileImage(FFireBaseToken fFireBaseToken,
                                                      @RequestParam("ProfileImage") MultipartFile file){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fUserInfoService.updateUserProfileImage(fFireBaseToken,file));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/FUserInfo/PwChange")
    public void userPwChange(FFireBaseToken fFireBaseToken, String pw){
        fUserInfoService.userPwChange(fFireBaseToken,pw);
    }


}
