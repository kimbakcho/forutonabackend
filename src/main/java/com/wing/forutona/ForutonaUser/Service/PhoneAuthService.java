package com.wing.forutona.ForutonaUser.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wing.forutona.CustomUtil.SHA256Util;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Domain.PhoneAuth;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.PhoneAuthDataRepository;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhoneAuthService {

    @Autowired
    PhoneAuthDataRepository phoneAuthDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Value("${forutona.sureMID}")
    String sureMID;

    @Value("${forutona.sureMdeptcode}")
    String sureMdeptcode;

    @Value("${forutona.smssretrieverappsign}")
    String smssretrieverappsign;

    @Value("${forutona.sureMFrom}")
    String sureMFrom;


    @Transactional
    public PhoneAuthResDto reqPhoneAuth(PhoneAuthReqDto reqDto) {
        List<PhoneAuth> phoneNumber =
                phoneAuthDataRepository.findPhoneAuthByInternationalizedPhoneNumberIs(reqDto.getInternationalizedPhoneNumber());
        if (phoneNumber.size() > 0 &&
                phoneNumber.get(0).getAuthRetryAvailableTime().isAfter(LocalDateTime.now())) {
            return new PhoneAuthResDto(phoneNumber.get(0));
        } else {
            if (phoneNumber.size() > 0) {
                phoneAuthDataRepository.deleteById(phoneNumber.get(0).getIdx());
                phoneAuthDataRepository.flush();
            }
            PhoneAuth phoneAuth = PhoneAuth.fromPhoneAuthReqDto(reqDto);
            phoneAuthDataRepository.save(phoneAuth);
            phoneAuthDataRepository.flush();
            SuerMSendsns(phoneAuth.getInternationalizedPhoneNumber(), phoneAuth.getAuthNumber(), phoneAuth.getIsoCode());
            return new PhoneAuthResDto(phoneAuth);
        }
    }

    public int SuerMSendsns(String phoneNumber, String authNumber, String isocode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        URI SureMuri = null;
        try {
            SureMuri = new URI("https://gw.surem.com/sms/v1/json");
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
        snsobject.put("from", sureMFrom.replaceAll("-", ""));
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


    @Transactional
    public PhoneAuthNumberResDto reqNumberAuthReq(PhoneAuthNumberReqDto reqDto) throws Exception {
        List<PhoneAuth> phoneNumber =
                phoneAuthDataRepository.findPhoneAuthByInternationalizedPhoneNumberIs(reqDto.getInternationalizedPhoneNumber());
        if (phoneNumber.size() > 0) {
            PhoneAuth phoneAuth = phoneNumber.get(0);
            if (phoneAuth.getAuthTime().isBefore(LocalDateTime.now())) {
                PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                resDto.setErrorFlag(true);
                resDto.setErrorCause("authTime Over");
                return resDto;
            } else if (phoneAuth.getAuthNumber().equals(reqDto.getAuthNumber())) {
                PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                resDto.setInternationalizedPhoneNumber(reqDto.getInternationalizedPhoneNumber());
                resDto.setPhoneNumber(reqDto.getPhoneNumber());
                resDto.setPhoneAuthToken(SHA256Util.getEncSHA256(reqDto.getInternationalizedPhoneNumber() + "Forutona123"));
                resDto.setErrorFlag(false);
                return resDto;
            } else {
                PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
                resDto.setErrorFlag(true);
                resDto.setErrorCause("인증 번호 불일치");
                return resDto;
            }
        } else {
            PhoneAuthNumberResDto resDto = new PhoneAuthNumberResDto();
            resDto.setErrorFlag(true);
            resDto.setErrorCause("don't have authHistory");
            return resDto;
        }

    }

    @Transactional
    public PwFindPhoneAuthResDto reqPwFindPhoneAuth(PwFindPhoneAuthReqDto reqDto) {

        UserRecord userByEmail = null;
        try {
            userByEmail = FirebaseAuth.getInstance().getUserByEmail(reqDto.getEmail());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        if (userByEmail != null) {
            FUserInfo fUserInfo = fUserInfoDataRepository.findById(userByEmail.getUid()).get();
            if (!fUserInfo.getPhoneNumber().equals(reqDto.getInternationalizedPhoneNumber())) {
                PwFindPhoneAuthResDto resDto = new PwFindPhoneAuthResDto();
                resDto.setError(true);
                resDto.setCause("MissMatchEmailAndPhone");
                return resDto;
            } else {
                List<PhoneAuth> phoneNumber =
                        phoneAuthDataRepository.findPhoneAuthByInternationalizedPhoneNumberIs(reqDto.getInternationalizedPhoneNumber());
                if (phoneNumber.size() > 0 &&
                        phoneNumber.get(0).getAuthRetryAvailableTime().isAfter(LocalDateTime.now())) {
                    PwFindPhoneAuthResDto resDto = new PwFindPhoneAuthResDto(phoneNumber.get(0));
                    resDto.setError(false);
                    resDto.setCause("");
                    resDto.setEmail(reqDto.getEmail());
                    return resDto;
                } else {
                    if (phoneNumber.size() > 0) {
                        phoneAuthDataRepository.deleteById(phoneNumber.get(0).getIdx());
                        phoneAuthDataRepository.flush();
                    }
                    PhoneAuth phoneAuth = PhoneAuth.fromPhoneAuthReqDto(reqDto);
                    phoneAuthDataRepository.save(phoneAuth);
                    PwFindPhoneAuthResDto resDto = new PwFindPhoneAuthResDto(phoneAuth);
                    resDto.setError(false);
                    resDto.setEmail(reqDto.getEmail());
                    resDto.setCause("");
                    SuerMSendsns(phoneAuth.getInternationalizedPhoneNumber(), phoneAuth.getAuthNumber(), phoneAuth.getIsoCode());
                    return resDto;
                }
            }
        } else {
            PwFindPhoneAuthResDto resDto = new PwFindPhoneAuthResDto();
            resDto.setError(true);
            resDto.setCause("don'tHaveUser");
            return resDto;
        }

    }

    @Transactional
    public PwFindPhoneAuthNumberResDto reqPwFindNumberAuth(PwFindPhoneAuthNumberReqDto reqDto) throws Exception {

        UserRecord userByEmail = null;
        try {
            userByEmail = FirebaseAuth.getInstance().getUserByEmail(reqDto.getEmail());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        if (userByEmail != null) {
            FUserInfo fUserInfo = fUserInfoDataRepository.findById(userByEmail.getUid()).get();
            if (!fUserInfo.getPhoneNumber().equals(reqDto.getInternationalizedPhoneNumber())) {
                PwFindPhoneAuthNumberResDto resDto = new PwFindPhoneAuthNumberResDto();
                resDto.setErrorFlag(true);
                resDto.setErrorCause("MissMatchEmailAndPhone");
                return resDto;
            } else {
                List<PhoneAuth> phoneNumber =
                        phoneAuthDataRepository.findPhoneAuthByInternationalizedPhoneNumberIs(reqDto.getInternationalizedPhoneNumber());
                if (phoneNumber.size() > 0) {
                    PhoneAuth phoneAuth = phoneNumber.get(0);
                    if (phoneAuth.getAuthTime().isBefore(LocalDateTime.now())) {
                        PwFindPhoneAuthNumberResDto resDto = new PwFindPhoneAuthNumberResDto();
                        resDto.setErrorFlag(true);
                        resDto.setErrorCause("authTime Over");
                        return resDto;
                    } else if (phoneAuth.getAuthNumber().equals(reqDto.getAuthNumber())) {
                        PwFindPhoneAuthNumberResDto resDto = new PwFindPhoneAuthNumberResDto();
                        resDto.setEmail(reqDto.getEmail());
                        resDto.setInternationalizedPhoneNumber(reqDto.getInternationalizedPhoneNumber());
                        resDto.setPhoneNumber(reqDto.getPhoneNumber());
                        resDto.setEmailPhoneAuthToken(SHA256Util.getEncSHA256(reqDto.getEmail() + reqDto.getInternationalizedPhoneNumber() + "Forutona123"));
                        resDto.setErrorFlag(false);
                        return resDto;
                    } else {
                        PwFindPhoneAuthNumberResDto resDto = new PwFindPhoneAuthNumberResDto();
                        resDto.setErrorFlag(true);
                        resDto.setErrorCause("인증 번호 불일치");
                        return resDto;
                    }
                } else {
                    throw new Exception("don't have authHistory");
                }
            }
        }
        return null;
    }

    @Transactional
    public PwChangeFromPhoneAuthResDto reqChangePwAuthPhone(PwChangeFromPhoneAuthReqDto reqDto) throws Exception {

            String sha256AuthToken = SHA256Util.getEncSHA256(reqDto.getEmail() + reqDto.getInternationalizedPhoneNumber() + "Forutona123");
            if (sha256AuthToken.equals(reqDto.getEmailPhoneAuthToken())) {
                UserRecord userByEmail = FirebaseAuth.getInstance().getUserByEmail(reqDto.getEmail());
                UserRecord.UpdateRequest userUpdate = new UserRecord.UpdateRequest(userByEmail.getUid());
                userUpdate.setPassword(reqDto.getPassword());
                FirebaseAuth.getInstance().updateUser(userUpdate);
                PwChangeFromPhoneAuthResDto resDto = new PwChangeFromPhoneAuthResDto();
                resDto.setErrorFlag(false);
                resDto.setCause("");
                resDto.setEmail(reqDto.getEmail());
                resDto.setInternationalizedPhoneNumber(reqDto.getInternationalizedPhoneNumber());
                return resDto;
            } else {
                PwChangeFromPhoneAuthResDto resDto = new PwChangeFromPhoneAuthResDto();
                resDto.setErrorFlag(true);
                resDto.setCause("TokenMissMatch");
                return resDto;
            }

    }
}
