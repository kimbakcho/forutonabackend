package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Service.FUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class FUserInfoController {

    @Autowired
    FUserInfoService fUserInfoService;

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @GetMapping(value = "/v1/ForutonaUser/Me")
    public ResponseBodyEmitter getMe(FFireBaseToken fFireBaseToken){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.getMe(emitter,fFireBaseToken);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/ForutonaUser/checkNickNameDuplication")
    public ResponseBodyEmitter checkNickNameDuplication(@RequestParam  String nickName){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.checkNickNameDuplication(emitter,nickName);
        return emitter;
    }

    @ResponseAddJsonHeader
    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/ForutonaUser/AccountUserInfo")
    public ResponseBodyEmitter updateAccountUserInfo(FFireBaseToken fFireBaseToken, @RequestBody FuserAccountUpdateReqdto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.updateAccountUserInfo(emitter,fFireBaseToken,reqDto);
        return emitter;
    }

    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/ForutonaUser/ProfileImage")
    public ResponseBodyEmitter updateUserProfileImage(FFireBaseToken fFireBaseToken,
                                                      @RequestParam("ProfileImage") MultipartFile file){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        fUserInfoService.updateUserProfileImage(emitter,fFireBaseToken,file);
        return emitter;
    }


    @AuthFireBaseJwtCheck
    @PutMapping(value = "/v1/ForutonaUser/PwChange")
    public ResponseBodyEmitter userPwChange(FFireBaseToken fFireBaseToken, @RequestBody  FUserInfoPwChangeReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.userPwChange(emitter,fFireBaseToken,reqDto);
        return emitter;
    }

    /**
     * 개인정보 해킹을 방지 해기 위해서 최소한의 정보만 주는 스타일 1번
     * 클라이언트 에서 다른 사람의 민감한 개인정보를 취득하는 것 막기 위한 API
     */
    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/ForutonaUser/UserInfoSimple1")
    public ResponseBodyEmitter getUserInfoSimple1(FUserReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.getUserInfoSimple1(emitter,reqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/ForutonaUser/SnsUserJoinCheckInfo")
    public ResponseBodyEmitter getSnsUserJoinCheckInfo(FUserSnSLoginReqDto snSLoginReqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.getSnsUserJoinCheckInfo(emitter,snSLoginReqDto);
        return emitter;
    }


    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/ForutonaUser/JoinUser")
    public ResponseBodyEmitter joinUser(@RequestBody FUserInfoJoinReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        fUserInfoService.joinUser(emitter,reqDto);
        return emitter;
    }


}
