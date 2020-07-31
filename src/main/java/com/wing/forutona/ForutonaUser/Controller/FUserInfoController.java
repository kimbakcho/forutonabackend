package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Service.FUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

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
    public void updateFireBaseMessageToken(@RequestParam String uid,@RequestParam String token){
        fUserInfoService.updateFireBaseMessageToken(uid, token);
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/FUserInfo/JoinUser")
    public FUserInfoJoinResDto joinUser(@RequestBody FUserInfoJoinReqDto reqDto) throws Exception {
        return fUserInfoService.joinUser(reqDto);
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/FUserInfo/SnsUserJoinCheckInfo")
    public FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(FUserSnSLoginReqDto snSLoginReqDto) throws Exception {
        return fUserInfoService.getSnsUserJoinCheckInfo(snSLoginReqDto);
    }


}
