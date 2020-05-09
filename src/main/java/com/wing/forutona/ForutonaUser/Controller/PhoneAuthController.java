package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.PhoneAuthNumberReqDto;
import com.wing.forutona.ForutonaUser.Dto.PhoneAuthReqDto;
import com.wing.forutona.ForutonaUser.Service.PhoneAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class PhoneAuthController {

    @Autowired
    PhoneAuthService phoneAuthService;

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/Req")
    public ResponseBodyEmitter reqPhoneAuth(@RequestBody  PhoneAuthReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        phoneAuthService.reqPhoneAuth(emitter,reqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/NumberAuthReq")
    public ResponseBodyEmitter reqNumberAuthReq(@RequestBody PhoneAuthNumberReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        phoneAuthService.reqNumberAuthReq(emitter,reqDto);
        return emitter;
    }

}
