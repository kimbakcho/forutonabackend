package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Service.PhoneAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/PwFindReq")
    public ResponseBodyEmitter reqPwFindPhoneAuth(@RequestBody PwFindPhoneAuthReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        phoneAuthService.reqPwFindPhoneAuth(emitter,reqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/PwFindNumberAuthReq")
    public ResponseBodyEmitter reqPwFindNumberAuth(@RequestBody PwFindPhoneAuthNumberReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        phoneAuthService.reqPwFindNumberAuth(emitter,reqDto);
        return emitter;
    }

    @ResponseAddJsonHeader
    @PutMapping(value = "/v1/PhoneAuth/changePwAuthPhone")
    public ResponseBodyEmitter reqChangePwAuthPhone(@RequestBody PwChangeFromPhoneAuthReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        phoneAuthService.reqChangePwAuthPhone(emitter,reqDto);
        return emitter;
    }



}
