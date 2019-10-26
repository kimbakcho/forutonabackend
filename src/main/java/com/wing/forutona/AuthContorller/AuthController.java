package com.wing.forutona.AuthContorller;

import com.wing.forutona.AuthDao.UserInfoDao;
import com.wing.forutona.AuthDto.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    UserInfoDao userInfoDao;

    @PostMapping(value = "/Auth/UpdateUser")
    int InsertUserInfo(@RequestBody Userinfo param) {
        return userInfoDao.InsertUserInfo(param);
    }

}
