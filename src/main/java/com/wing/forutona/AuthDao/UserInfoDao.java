package com.wing.forutona.AuthDao;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.AuthDto.Phoneauthtable;
import com.wing.forutona.AuthDto.UserInfoMain;
import com.wing.forutona.AuthDto.Userinfo;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import com.wing.forutona.Prefrerance;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import static java.nio.charset.StandardCharsets.UTF_8;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class UserInfoDao {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    @Autowired
    GoogleStorgeAdmin googlesotrageAdmin;

    public int InsertUserInfo(UserInfoMain param) {
        UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);

        return mapper.insert(param);
    }

    public String SnsLoginFireBase(UserInfoMain param)  {
        RestTemplate restTemplate = new RestTemplate();
        if(param.getSnsservice().equals("Naver") ){
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.AUTHORIZATION,"Bearer "+param.getSnstoken());
            ResponseEntity<String> response = new RestTemplate().exchange("https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET, new HttpEntity(header), String.class);
            JSONObject obj = new JSONObject(response.getBody());
            String uid = param.getSnsservice()  + obj.getJSONObject("response").getString("id");
            return GetCustomToken(param,  uid);

        }else if(param.getSnsservice().equals("Kakao") ){
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.AUTHORIZATION,"Bearer "+param.getSnstoken());
            header.add(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=utf-8");
            ResponseEntity<String> response = new RestTemplate().exchange("https://kapi.kakao.com/v2/user/me ",
                    HttpMethod.POST, new HttpEntity(header), String.class);
            JSONObject obj = new JSONObject(response.getBody());
            String uid = param.getSnsservice() +obj.getInt("id");
            return GetCustomToken(param, uid);
        }else if(param.getSnsservice().equals("Facebook") ) {
            String geturl = "https://graph.facebook.com/v2.12/me?fields=name,first_name,last_name,email&access_token="+param.getSnstoken();
            ResponseEntity<String> response  = new RestTemplate().getForEntity(geturl,String.class);
            JSONObject obj = new JSONObject(response.getBody());
            String uid = param.getSnsservice() +obj.getString("id");
            return GetCustomToken(param, uid);
        }
        return "";
    }

    private String GetCustomToken(UserInfoMain param, String uid) {
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

     public String UploadProfileImage(MultipartHttpServletRequest request) throws IOException {
        System.out.println(request.getFileMap());
         List<MultipartFile> getfile = request.getMultiFileMap().get("ProfileImage");
         Storage storage =  googlesotrageAdmin.GetStorageInstance();
         String OriginalFile = getfile.get(0).getOriginalFilename();
         int extentionindex = OriginalFile.lastIndexOf(".");
         UUID uuid = UUID.randomUUID();
         String savefilename = "";
         if(extentionindex > 0){
             String extention = OriginalFile.substring(extentionindex);
             savefilename = uuid.toString() + extention;
         }else {
             savefilename = uuid.toString();
         }
         BlobId blobId = BlobId.of("publicforutona", "profileimage/"+savefilename);
         BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
         Blob blob = storage.create(blobInfo, getfile.get(0).getBytes());
        return "https://storage.googleapis.com/publicforutona/profileimage/"+savefilename;
     }

    public Userinfo GetUserInfoMain(String Authtoken,String uid ){
        String requesttoken = Authtoken.replace("Bearer ","");
        FirebaseToken firebasetoken  = fireBaseAdmin.VerifyIdToken(requesttoken);
        UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
        Userinfo userinfo = mapper.selectByPrimaryKey(firebasetoken.getUid());
        return userinfo;
    }

    public void requestAuthPhoneNumber(Phoneauthtable phone){
        System.out.println(phone.getUuid());
        System.out.println(phone.getPhonenumber());
        PhoneauthtableMapper mapper = sqlSession.getMapper(PhoneauthtableMapper.class);
        try{
            mapper.insert(phone);
        }catch (DuplicateKeyException ex){
            Phoneauthtable phoneauthtable = mapper.selectByPrimaryKey(phone.getUuid());
            phoneauthtable.setUpdatetime(new Date());
            phoneauthtable.setRequestcount(phoneauthtable.getRequestcount()+1);
            mapper.updateByPrimaryKey(phoneauthtable);
            System.out.println(ex.getMessage());
        }
    }



}
