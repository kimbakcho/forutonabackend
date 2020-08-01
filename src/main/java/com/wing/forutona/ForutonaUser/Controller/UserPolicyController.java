package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.ForutonaUser.Dto.UserPolicyResDto;
import com.wing.forutona.ForutonaUser.Service.UserPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
public class UserPolicyController {

    @Autowired
    UserPolicyService userPolicyService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/UserPolicy/{policy}")
    UserPolicyResDto getForutonaUserUserPolicy(@PathVariable String policy){
        return userPolicyService.getPolicy(policy);
    }

}
