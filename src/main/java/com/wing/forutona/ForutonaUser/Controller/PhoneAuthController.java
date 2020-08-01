package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Service.PhoneAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequiredArgsConstructor
public class PhoneAuthController {

    final PhoneAuthService phoneAuthService;

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/Req")
    public PhoneAuthResDto reqPhoneAuth(@RequestBody PhoneAuthReqDto reqDto){
        return phoneAuthService.reqPhoneAuth(reqDto);
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/NumberAuthReq")
    public PhoneAuthNumberResDto reqNumberAuthReq(@RequestBody PhoneAuthNumberReqDto reqDto) throws Exception {
        return phoneAuthService.reqNumberAuthReq(reqDto);
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/PwFindReq")
    public PwFindPhoneAuthResDto reqPwFindPhoneAuth(@RequestBody PwFindPhoneAuthReqDto reqDto){
        return phoneAuthService.reqPwFindPhoneAuth(reqDto);
    }

    @ResponseAddJsonHeader
    @PostMapping(value = "/v1/PhoneAuth/PwFindNumberAuthReq")
    public PwFindPhoneAuthNumberResDto reqPwFindNumberAuth(@RequestBody PwFindPhoneAuthNumberReqDto reqDto) throws Exception {
        return phoneAuthService.reqPwFindNumberAuth(reqDto);
    }

    @ResponseAddJsonHeader
    @PutMapping(value = "/v1/PhoneAuth/changePwAuthPhone")
    public PwChangeFromPhoneAuthResDto reqChangePwAuthPhone(@RequestBody PwChangeFromPhoneAuthReqDto reqDto) throws Exception {
        return phoneAuthService.reqChangePwAuthPhone(reqDto);
    }

}
