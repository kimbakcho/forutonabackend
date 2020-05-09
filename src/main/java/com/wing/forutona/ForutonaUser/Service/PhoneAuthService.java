package com.wing.forutona.ForutonaUser.Service;

import com.wing.forutona.ForutonaUser.Domain.PhoneAuth;
import com.wing.forutona.ForutonaUser.Dto.PhoneAuthNumberReqDto;
import com.wing.forutona.ForutonaUser.Dto.PhoneAuthNumberResDto;
import com.wing.forutona.ForutonaUser.Dto.PhoneAuthReqDto;
import com.wing.forutona.ForutonaUser.Dto.PhoneAuthResDto;
import com.wing.forutona.ForutonaUser.Repository.PhoneAuthDataRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.shaded.com.ongres.scram.common.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhoneAuthService {

    @Autowired
    PhoneAuthDataRepository phoneAuthDataRepository;

    @Value("${forutona.sureMID}")
    String sureMID;

    @Value("${forutona.sureMdeptcode}")
    String sureMdeptcode;

    @Value("${forutona.smssretrieverappsign}")
    String smssretrieverappsign;

    @Value("${forutona.sureMFrom}")
    String sureMFrom;

    @Async
    @Transactional
    public void reqPhoneAuth(ResponseBodyEmitter emitter, PhoneAuthReqDto reqDto) {
        try {
            List<PhoneAuth> phoneNumber =
                    phoneAuthDataRepository.findPhoneAuthByInternationalizedPhoneNumberIs(reqDto.getInternationalizedPhoneNumber());
            if (phoneNumber.size() > 0 &&
                    phoneNumber.get(0).getAuthRetryAvailableTime().isAfter(LocalDateTime.now())) {
                emitter.send(new PhoneAuthResDto(phoneNumber.get(0)));
            } else {
                if(phoneNumber.size() > 0){
                    phoneAuthDataRepository.deleteById(phoneNumber.get(0).getIdx());
                }
                PhoneAuth phoneAuth = new PhoneAuth(reqDto);
                phoneAuthDataRepository.save(phoneAuth);
                SuerMSendsns(phoneAuth.getInternationalizedPhoneNumber(),phoneAuth.getAuthNumber(),phoneAuth.getIsoCode());
                emitter.send(new PhoneAuthResDto(phoneAuth));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    public int SuerMSendsns(String phoneNumber, String authNumber, String isocode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        URI SureMuri = null;
        try {
            SureMuri = new URI("https://rest.surem.com/sms/v1/json");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Charset utf8 = Charset.forName("UTF-8");
        MediaType mediaType = new MediaType("application", "json", utf8);
        headers.setContentType(mediaType);
        JSONObject snsobject = new JSONObject();
        snsobject.put("usercode", sureMID);
        snsobject.put("deptcode", sureMdeptcode);
        JSONArray messagearray = new JSONArray();
        JSONObject messageobj = new JSONObject();
        if (isocode.equals("KR")) {
            messageobj.put("to", phoneNumber.replace("+82", "0"));
        }
        messagearray.put(messageobj);
        snsobject.put("messages", messagearray);
        String sendmessage = "<#>\n";
        if (isocode.equals("KR")) {
            sendmessage += "[인증번호:" + authNumber + "] FORUTONA 계정 인증번호 입니다. [FORUTONA]\n";
        }
        sendmessage += smssretrieverappsign;
        snsobject.put("text", sendmessage);
        snsobject.put("from", sureMFrom.replaceAll("-",""));
        System.out.println(snsobject.toString());
        HttpEntity<String> request = new HttpEntity<>(snsobject.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SureMuri, request, String.class);
        JSONObject responsesns = new JSONObject(response.getBody());
        String snsreslutcode = responsesns.getString("code");
        if (snsreslutcode.equals("200")) {
            return 1;
        }
        return 0;
    }

    @Async
    @Transactional
    public void reqNumberAuthReq(ResponseBodyEmitter emitter, PhoneAuthNumberReqDto reqDto) {
        try{
            List<PhoneAuth> phoneNumber =
                    phoneAuthDataRepository.findPhoneAuthByInternationalizedPhoneNumberIs(reqDto.getInternationalizedPhoneNumber());
            if(phoneNumber.size() > 0){
                PhoneAuth phoneAuth = phoneNumber.get(0);
                if(phoneAuth.getAuthTime().isBefore(LocalDateTime.now())){
                    PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                    resDto.setErrorFlag(true);
                    resDto.setErrorCause("authTime Over");
                    emitter.send(resDto);
                }else if(phoneAuth.getAuthNumber().equals(reqDto.getAuthNumber())){
                    PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                    resDto.setInternationalizedPhoneNumber(reqDto.getInternationalizedPhoneNumber());
                    resDto.setPhoneNumber(reqDto.getPhoneNumber());
                    resDto.setPhoneAuthToken(getEncSHA256(reqDto.getInternationalizedPhoneNumber()+"Forutona123"));
                    resDto.setErrorFlag(false);
                    emitter.send(resDto);
                }else {
                    PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                    resDto.setErrorFlag(true);
                    resDto.setErrorCause("Wrong AuthNumber");
                    emitter.send(resDto);
                }
            }else {
                PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                resDto.setErrorFlag(true);
                resDto.setErrorCause("don't have authHistory");
                emitter.send(resDto);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            emitter.complete();
        }

    }

    public String getEncSHA256(String txt) throws Exception{
        StringBuffer sbuf = new StringBuffer();
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        mDigest.update(txt.getBytes());
        byte[] msgStr = mDigest.digest() ;
        for(int i=0; i < msgStr.length; i++){
            byte tmpStrByte = msgStr[i];
            String tmpEncTxt = Integer.toString((tmpStrByte & 0xff) + 0x100, 16).substring(1);

            sbuf.append(tmpEncTxt) ;
        }
        return sbuf.toString();
    }
}
