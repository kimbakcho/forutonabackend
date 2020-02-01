package com.wing.forutona.AuthDao;

import com.google.cloud.storage.*;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.AuthDto.Phoneauthtable;
import com.wing.forutona.AuthDto.PhoneauthtableCustom;
import com.wing.forutona.AuthDto.UserInfoMain;
import com.wing.forutona.AuthDto.Userinfo;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import com.wing.forutona.Prefrerance;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.*;

@Component
public class UserInfoDao {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    @Autowired
    GoogleStorgeAdmin googlesotrageAdmin;

    public int InsertUserInfo(UserInfoMain param,String Authtoken) {
        int result = 0;
        String returncode = "";
        String authtoken =  Authtoken.replace("Bearer ","");
        if(fireBaseAdmin.VerifyIdToken(authtoken) == null){
            return 0;
        }
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(param.getPhonenumber().getBytes());
            returncode =  bytesToHex(md.digest());
        }catch (Exception ex){

        }
        if(param.getPhoneauthcheckcode().equals(returncode) ){
            UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
            param.setExppoint(0.0);
            result = mapper.insert(param);
        }else {
            result = 0;
        }
        return result;
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
            param.setExppoint(0.0);
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

    public Userinfo GetUserInfoMain(String Authtoken, String uid ){
        String requesttoken = Authtoken.replace("Bearer ","");
        FirebaseToken firebasetoken  = fireBaseAdmin.VerifyIdToken(requesttoken);
        UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
        Userinfo userinfo = mapper.selectByPrimaryKey(firebasetoken.getUid());
        return userinfo;
    }

    public int requestFindAuthPhoneNumber(Phoneauthtable phone){
        UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
        Userinfo userinfo = mapper.selectByPrimaryKey(phone.getUuid());
        if(userinfo.getPhonenumber().equals(phone.getPhonenumber()) ){
            this.requestAuthPhoneNumber(phone);
            return 1;
        }else {
            return 0;
        }
    }


    public void requestAuthPhoneNumber(Phoneauthtable phone){
        System.out.println(phone.getUuid());
        System.out.println(phone.getPhonenumber());
        PhoneauthtableCustomMapper mapper1 = sqlSession.getMapper(PhoneauthtableCustomMapper.class);
        PhoneauthtableCustom customdata = new PhoneauthtableCustom();
        customdata.setEventuuid("PHONEPW"+phone.getUuid().replaceAll("-",""));
        customdata.setUuid(phone.getUuid());
        try{
            mapper1.DropRemoveEvent(customdata);
        }catch (Exception ex){

        }

        double dValue = Math.random();
        int iValue = (int)(dValue * 100000)+100000;
        phone.setAuthnumber(String.format("%d",iValue));

        //해당 부분 전화 번호 Send 만 해주면됨.
        SuerMSendsns(phone.getPhonenumber(),phone.getAuthnumber(),phone.getIsocode());
//        SureSMSAPI sms = new SureSMSAPI() {
//            @Override
//            public void report(SureSMSDeliveryReport dr) {
//                // 통신사로부터 받은 결과 데이터
//                System.out.print("msgkey="+dr.getMember()+"\t");	// 발송한 메시지의 키값
//                System.out.print("result="+dr.getResult()+"\t");	// '2': 통신사 결과 성공.  '4': 통신사 결과 실패
//                System.out.print("errorcode="+dr.getErrorCode()+"\t");	// 통신사 에러코드 ( 101 : 성공 )
//                System.out.print("recvtime="+dr.getRecvDate()+dr.getRecvTime()+"\t");	// 단말기 도착 시간
//                System.out.println();
//            }
//        };
//        sms.sendMain(0,Prefrerance.sureMID,Prefrerance.sureMdeptcode,Prefrerance.sureMFrom,)
        PhoneauthtableMapper mapper = sqlSession.getMapper(PhoneauthtableMapper.class);
        try{
            mapper.insert(phone);
        }catch (DuplicateKeyException ex){
            Phoneauthtable phoneauthtable = mapper.selectByPrimaryKey(phone.getUuid());
            phoneauthtable.setUpdatetime(new Date());
            phoneauthtable.setAuthnumber(phone.getAuthnumber());
            if(phoneauthtable.getRequestcount() == null){
                phoneauthtable.setRequestcount(0);
            }
            phoneauthtable.setRequestcount(phoneauthtable.getRequestcount()+1);
            mapper.updateByPrimaryKey(phoneauthtable);
            System.out.println(ex.getMessage());
        }


        //5분뒤 DB에서 삭제
        mapper1.CreateRemoveEvent(customdata);
    }

    public String GetEmailtoUid(String email) throws FirebaseAuthException {
        return fireBaseAdmin.getUserByEmail(email).getUid();
    }

    public String requestAuthVerificationPhoneNumber(Phoneauthtable phone){
        PhoneauthtableMapper mapper = sqlSession.getMapper(PhoneauthtableMapper.class);
        Phoneauthtable getphone = mapper.selectByPrimaryKey(phone.getUuid());
        String returncode = "";
        if(getphone.getAuthnumber().equals(phone.getAuthnumber())){
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(phone.getPhonenumber().getBytes());
                returncode =  bytesToHex(md.digest());
            }catch (Exception ex){
                returncode =  "false";
            }
        }else {
            returncode =  "false";
        }
        return returncode;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public int SuerMSendsns(String Phonenumber,String authNumber,String isocode){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        URI SureMuri = null;
        try {
            SureMuri = new URI("https://rest.surem.com/sms/v1/json");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Charset utf8 = Charset.forName("UTF-8");
        MediaType mediaType = new MediaType("application","json",utf8);
        headers.setContentType(mediaType);
        JSONObject snsobject = new JSONObject();
        snsobject.put("usercode",Prefrerance.sureMID);
        snsobject.put("deptcode",Prefrerance.sureMdeptcode);
        JSONArray messagearray = new JSONArray();
        JSONObject messageobj = new JSONObject();
        if(isocode.equals("KR")){
            messageobj.put("to",Phonenumber.replace("+82","0"));
        }
        messagearray.put(messageobj);
        snsobject.put("messages",messagearray);
        String sendmessage = "<#>\n";
        if(isocode.equals("KR")){
            sendmessage += "[인증번호:"+authNumber+"] FORUTONA 계정 인증번호 입니다. [FORUTONA]\n";
        }
        sendmessage += Prefrerance.smssretrieverappsign;
        snsobject.put("text",sendmessage);
        snsobject.put("from",Prefrerance.sureMFrom);
        System.out.println(snsobject.toString());
        HttpEntity<String> request = new HttpEntity<>(snsobject.toString(),headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SureMuri,request, String.class);
        JSONObject responsesns = new JSONObject(response.getBody());
        String snsreslutcode = responsesns.getString("code");
        if(snsreslutcode.equals("200")){
            return 1;
        }
        return 0;
    }

     public  UserInfoMain getUsePasswordFindPhoneInfoByemail(String email){
         UserInfoMain reslutuserinfo;
        try {
             UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
             Userinfo userinfo = mapper.selectByPrimaryKey(fireBaseAdmin.getUserByEmail(email).getUid());
             reslutuserinfo = new UserInfoMain();
             reslutuserinfo.setEmail(userinfo.getEmail());
             reslutuserinfo.setPhonenumber(userinfo.getPhonenumber());
             reslutuserinfo.setIsocode(userinfo.getIsocode());
         }catch (Exception ex){
             return null;
         }
         return reslutuserinfo;
     }

     public int passwrodChangefromphone(UserInfoMain userinfo){
         UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);
         Userinfo dbinfo = mapper.selectByPrimaryKey(userinfo.getUid());
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(dbinfo.getPhonenumber().getBytes());
            String returncode =  bytesToHex(md.digest());
            //번호 위조 체크
            if(returncode.equals(userinfo.getPhoneauthcheckcode())) {
                return fireBaseAdmin.changeEmailUserPassword(userinfo.getEmail(),userinfo.getPassword());
            }
            return 0;
        }catch (Exception ex){
            return 0;
        }
     };

    public int updateCurrentPosition(UserInfoMain userinfo){
        UserinfoMainMapper mapper  = sqlSession.getMapper(UserinfoMainMapper.class);
        return mapper.updateCurrentPosition(userinfo);
    }
    public int updateFCMtoken(UserInfoMain userinfo){
        UserinfoMainMapper mapper  = sqlSession.getMapper(UserinfoMainMapper.class);
        return mapper.updateFCMtoken(userinfo);
    }
}
