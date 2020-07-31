package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Service.FAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class FAccountController {

    final FAccountService fAccountService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/ForutonaUser/checkNickNameDuplication")
    public ResponseBodyEmitter checkNickNameDuplication(@RequestParam  String nickName){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                emitter.send(fAccountService.checkNickNameDuplication(nickName));
                emitter.complete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/ForutonaUser/AccountUserInfo")
    public ResponseBodyEmitter updateAccountUserInfo(FFireBaseToken fFireBaseToken, @RequestBody FuserAccountUpdateReqdto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fAccountService.updateAccountUserInfo(emitter,fFireBaseToken,reqDto);
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/ForutonaUser/ProfileImage")
    public ResponseBodyEmitter updateUserProfileImage(FFireBaseToken fFireBaseToken,
                                                      @RequestParam("ProfileImage") MultipartFile file){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fAccountService.updateUserProfileImage(emitter,fFireBaseToken,file);
        return emitter;
    }


    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/ForutonaUser/PwChange")
    public ResponseBodyEmitter userPwChange(FFireBaseToken fFireBaseToken, @RequestBody  FUserInfoPwChangeReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fAccountService.userPwChange(emitter,fFireBaseToken,reqDto);
        return emitter;
        //TODO 이동중
    }



}
