package com.wing.forutona.AuthContorller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.auth.UserInfo;
import com.wing.forutona.AuthDao.UserInfoDao;
import com.wing.forutona.AuthDto.Phoneauthtable;
import com.wing.forutona.AuthDto.UserInfoMain;
import com.wing.forutona.AuthDto.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class AuthController {

    @Autowired
    UserInfoDao userInfoDao;

    @PostMapping(value = "/api/v1/Auth/InsertUserInfo")
    int InsertUserInfo(@RequestBody UserInfoMain param,@RequestHeader(value = "authorization") String Authtoken) {
        return userInfoDao.InsertUserInfo(param,Authtoken);
    }
    @PostMapping(value = "/api/v1/Auth/SnsLoginFireBase")
    String SnsLoginFireBase(@RequestBody UserInfoMain param) {
        return userInfoDao.SnsLoginFireBase(param);
    }

    @GetMapping(value = "/api/v1/Auth/GetUid")
    String GetFirebaseUid(@RequestParam String Uid) {
        return userInfoDao.GetFirebaseUid(Uid);
    }

    @PostMapping(value="/api/v1/Auth/UploadProfileImage")
    String UploadProfileImage(MultipartHttpServletRequest request) throws IOException {
        return userInfoDao.UploadProfileImage(request);
    }

    @GetMapping(value="/api/v1/Auth/GetUserInfoMain")
    Userinfo GetUserInfoMain(@RequestHeader(value = "authorization") String Authtoken,@RequestParam(value = "uid")String uid )
    {
        return userInfoDao.GetUserInfoMain(Authtoken,uid);
    }
    @PostMapping(value="/api/v1/Auth/requestAuthPhoneNumber")
    void requestAuthPhoneNumber(@RequestBody Phoneauthtable body)
    {
        userInfoDao.requestAuthPhoneNumber(body);
    }

    @PostMapping(value="/api/v1/Auth/requestAuthVerificationPhoneNumber")
    String requestAuthVerificationPhoneNumber(@RequestBody Phoneauthtable body){
        return userInfoDao.requestAuthVerificationPhoneNumber(body);
    }

    @GetMapping(value="/api/v1/Auth/getUsePasswordFindPhoneInfoByemail")
    UserInfoMain getUsePasswordFindPhoneInfoByemail(@RequestParam String email){
        return userInfoDao.getUsePasswordFindPhoneInfoByemail(email);
    }
}
