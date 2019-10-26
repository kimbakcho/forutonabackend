package com.wing.forutona.AuthDao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            System.out.println(response.getBody());
            JSONObject obj = new JSONObject(response.getBody());
            String uid = obj.getJSONObject("response").getString("id");
            UserRecord recode;
            try {
                recode = fireBaseAdmin.getUser(param.getUid());
            }catch (Exception ex){
                recode = null;
            }

            String customtoken = fireBaseAdmin.GetUserInfoCustomToken(param);
            if(recode == null){
                UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
                int result =  mapper.insert(param);
                if(result == 0){
                    return "";
                }
            }
            return  customtoken;
        }
        return "";
    }



    public String GetFirebaseUid(String uid){
        try {
            System.out.println("GetFirebaseUid2");
            UserRecord recode = fireBaseAdmin.getUser(uid);
            return recode.getUid();
        }catch (Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
            return "";
        }

    }


}
