package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.AuthFireBaseJwtCheck;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Service.FUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
