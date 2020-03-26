package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoQueryRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoRepository;
import com.wing.forutona.ForutonaUser.Service.FUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FUserInfoController {

    @Autowired
    FUserInfoService fUserInfoService;

    @GetMapping(value = "/v1/ForutonaUser/Basic/")
    public FUserInfoDto getBasicUserInfo(FUserReqDto fUserReqDto){
        return fUserInfoService.getBasicUserInfo(fUserReqDto);
    }

}
