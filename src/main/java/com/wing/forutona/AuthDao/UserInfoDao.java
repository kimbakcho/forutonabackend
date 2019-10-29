package com.wing.forutona.AuthDao;

import com.google.firebase.auth.UserRecord;
import com.wing.forutona.AuthDto.Userinfo;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class UserInfoDao {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    public int InsertUserInfo(Userinfo param) {
        UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);

        return mapper.insert(param);
    }

    public String SnsLoginFireBase(Userinfo param)  {
        RestTemplate restTemplate = new RestTemplate();
        if(param.getSnsservice().equals("Naver") ){
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.AUTHORIZATION,"Bearer "+param.getSnstoken());
            ResponseEntity<String> response = new RestTemplate().exchange("https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET, new HttpEntity(header), String.class);

            JSONObject obj = new JSONObject(response.getBody());
            String uid = "Naver"  + obj.getJSONObject("response").getString("id");
            return GetCustomToken(param,  uid);

        }else if(param.getSnsservice().equals("Kakao") ){
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.AUTHORIZATION,"Bearer "+param.getSnstoken());
            header.add(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=utf-8");
            ResponseEntity<String> response = new RestTemplate().exchange("https://kapi.kakao.com/v2/user/me ",
                    HttpMethod.POST, new HttpEntity(header), String.class);
            System.out.println(response.getBody());
            JSONObject obj = new JSONObject(response.getBody());
            String uid = "Kakao" +obj.getInt("id");
            return GetCustomToken(param, uid);
        }
        return "";
    }

    private String GetCustomToken(Userinfo param, String uid) {
        UserRecord recode;
        try {
            recode = fireBaseAdmin.getUser(uid);
        }catch (Exception ex){
            recode = null;
        }
        String customtoken;

        if(recode == null){
            customtoken = fireBaseAdmin.GetUserInfoCustomToken(param);
            UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
            int result =  mapper.insert(param);
            if(result == 0){
                return "";
            }
        }else {
            customtoken = fireBaseAdmin.GetUserInfoCustomToken(recode.getUid());
        }
        return customtoken;
    }


    public String GetFirebaseUid(String uid){
        try {
            UserRecord recode = fireBaseAdmin.getUser(uid);
            return recode.getUid();
        }catch (Exception ex){
            System.out.println(ex);
            return "";
        }

    }




}
