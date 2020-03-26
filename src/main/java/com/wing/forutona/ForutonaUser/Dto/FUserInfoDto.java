package com.wing.forutona.ForutonaUser.Dto;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FUserInfoDto {
    private String uid;
    private String nickName;
    private String profilePicktureUrl;
    private long gender;
    private LocalDateTime ageDate;
    private String email;
    private boolean forutonaAgree;
    private boolean privateAgree;
    private boolean positionAgree;
    private boolean martketingAgree;
    private boolean ageLimitAgree;
    private String snsService;
    private String phoneNumber;
    private String isoCode;
    private double latitude;
    private double longitude;
    private LocalDateTime positionUpdateTime;
    private double userLevel;
    private double expPoint;
    private String fCMtoken;
    private LocalDateTime joinTime;
    private long followCount;
    private long backOut;
    private LocalDateTime lastBackOutTime;

//    public FUserInfoDto(String uid, String nickName, String profilePicktureUrl, long gender, LocalDateTime ageDate, String email, String snsService, Double userLevel, Double expPoint, Integer followCount) {
//        this.uid = uid;
//        this.nickName = nickName;
//        this.profilePicktureUrl = profilePicktureUrl;
//        this.gender = gender;
//        this.ageDate = ageDate;
//        this.email = email;
//        this.snsService = snsService;
//        this.userLevel = userLevel;
//        this.expPoint = expPoint;
//        this.followCount = followCount;
//    }


}
