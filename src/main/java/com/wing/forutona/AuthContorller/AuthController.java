package com.wing.forutona.AuthContorller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.AuthDao.UserInfoDao;
import com.wing.forutona.AuthDto.Phoneauthtable;
import com.wing.forutona.AuthDto.UserInfoMain;
import com.wing.forutona.AuthDto.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@RestController
public class AuthController {

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

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

     @GetMapping(value = "/api/v1/Auth/GetEmailtoUid")
    String GetEmailtoUid(@RequestParam String email) throws FirebaseAuthException {
        return userInfoDao.GetEmailtoUid(email);
    }

    @PostMapping(value="/api/v1/Auth/UploadProfileImage")
    String UploadProfileImage(MultipartHttpServletRequest request) throws IOException {
        return userInfoDao.UploadProfileImage(request);
    }

    @GetMapping(value="/api/v1/Auth/GetUserInfoMain")
    Userinfo GetUserInfoMain(@RequestHeader(value = "authorization") String Authtoken, @RequestParam(value = "uid")String uid )
    {
        return userInfoDao.GetUserInfoMain(Authtoken,uid);
    }
    @PostMapping(value="/api/v1/Auth/requestAuthPhoneNumber")
    int requestAuthPhoneNumber(@RequestBody Phoneauthtable body)
    {
        return userInfoDao.requestAuthPhoneNumber(body);
    }

    @PostMapping(value="/api/v1/Auth/requestFindAuthPhoneNumber")
    int requestFindAuthPhoneNumber(@RequestBody Phoneauthtable phone) {
        return userInfoDao.requestFindAuthPhoneNumber(phone);
    }

    @PostMapping(value="/api/v1/Auth/requestAuthVerificationPhoneNumber")
    String requestAuthVerificationPhoneNumber(@RequestBody Phoneauthtable body){
        return userInfoDao.requestAuthVerificationPhoneNumber(body);
    }

    @GetMapping(value="/api/v1/Auth/getUsePasswordFindPhoneInfoByemail")
    UserInfoMain getUsePasswordFindPhoneInfoByemail(@RequestParam String email){
        return userInfoDao.getUsePasswordFindPhoneInfoByemail(email);
    }

    @PostMapping(value="/api/v1/Auth/passwrodChangefromphone")
    int passwrodChangefromphone(@RequestBody UserInfoMain userinfo){
        return userInfoDao.passwrodChangefromphone(userinfo);
    }

    @PostMapping(value="/api/v1/Auth/updateCurrentPosition")
    int updateCurrentPosition(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody UserInfoMain userinfo){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(userinfo.getUid())) {
            return userInfoDao.updateCurrentPosition(userinfo);
        }else {
            return 0;
        }
    }

    @PostMapping(value = "/api/v1/Auth/updateFCMtoken")
    int updateFCMtoken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody UserInfoMain userinfo ){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(userinfo.getUid())) {
            return userInfoDao.updateFCMtoken(userinfo);
        }else {
            return 0;
        }
    }
}
