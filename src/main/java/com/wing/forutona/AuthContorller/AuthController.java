package com.wing.forutona.AuthContorller;

import com.wing.forutona.AuthDao.UserInfoDao;
import com.wing.forutona.AuthDto.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    UserInfoDao userInfoDao;

    @PostMapping(value = "/api/v1/Auth/InsertUserInfo")
    int InsertUserInfo(@RequestBody Userinfo param) {
        return userInfoDao.InsertUserInfo(param);
    }
    @PostMapping(value = "/api/v1/Auth/SnsLoginFireBase")
    String SnsLoginFireBase(@RequestBody Userinfo param) {
        return userInfoDao.SnsLoginFireBase(param);
    }

    @GetMapping(value = "/api/v1/Auth/GetUid")
    String GetFirebaseUid(@RequestParam String Uid) {
        return userInfoDao.GetFirebaseUid(Uid);
    }
}
