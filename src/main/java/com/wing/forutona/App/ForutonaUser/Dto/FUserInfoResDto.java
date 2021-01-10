package com.wing.forutona.App.ForutonaUser.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Domain.GenderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FUserInfoResDto {
    private String uid;
    private String nickName;
    private String profilePictureUrl;
    private String backGroundImageUrl;
    private GenderType gender;
    private LocalDate ageDate;
    private String email;
    private Boolean forutonaAgree;
    private Boolean privateAgree;
    private Boolean positionAgree;
    private Boolean martketingAgree;
    private Boolean ageLimitAgree;
    private Boolean forutonaManagementAgree;
    private String snsService;
    private String phoneNumber;
    private String isoCode;
    private Double latitude;
    private Double longitude;
    private LocalDateTime positionUpdateTime;
    private Double userLevel;
    private Double expPoint;
    private String fCMtoken;
    private LocalDateTime joinTime;
    private Long followerCount;
    private Long followingCount;
    private Long backOut;
    private LocalDateTime lastBackOutTime;
    private String selfIntroduction;
    private Double cumulativeInfluence;
    private Double uPoint;
    private Double naPoint;
    private Long historyOpenAll;
    private Long historyOpenFollowSponsor;
    private Long historyOpenNoOpen;
    private Long sponsorHistoryOpenAll;
    private Long sponsorHistoryOpenSponAndFollowFromMe;
    private Long sponsorHistoryOpenSponNoOpen;
    private Boolean alarmChatMessage;
    private Boolean alarmContentReply;
    private Boolean alarmReplyAndReply;
    private Boolean alarmFollowNewContent;
    private Boolean alarmSponNewContent;
    private Long deactivation;
    int maliciousCount ;
    LocalDateTime stopPeriod;
    String maliciousCause;
    Boolean maliciousMessageCheck;

    @QueryProjection
    public FUserInfoResDto(FUserInfo fUserInfo){
        this.uid = fUserInfo.getUid();
        this.nickName = fUserInfo.getNickName();
        this.profilePictureUrl = fUserInfo.getProfilePictureUrl();
        this.backGroundImageUrl = fUserInfo.getBackGroundImageUrl();
        this.gender  = fUserInfo.getGender();
        this.ageDate = fUserInfo.getAgeDate();
        this.email = fUserInfo.getEmail();
        this.forutonaAgree = fUserInfo.getForutonaAgree();
        this.forutonaManagementAgree = fUserInfo.getForutonaManagementAgree();
        this.privateAgree = fUserInfo.getPrivateAgree();
        this.positionAgree = fUserInfo.getPositionAgree();
        this.martketingAgree = fUserInfo.getMartketingAgree();
        this.ageLimitAgree = fUserInfo.getAgeLimitAgree();
        this.snsService = fUserInfo.getSnsService();
        this.phoneNumber = fUserInfo.getPhoneNumber();
        this.isoCode = fUserInfo.getIsoCode();
        this.latitude = fUserInfo.getLatitude();
        this.longitude = fUserInfo.getLongitude();
        this.positionUpdateTime = fUserInfo.getPositionUpdateTime();
        this.userLevel = fUserInfo.getUserLevel();
        this.expPoint = fUserInfo.getExpPoint();
        this.fCMtoken = fUserInfo.getFCMtoken();
        this.joinTime = fUserInfo.getJoinTime();
        this.followerCount = fUserInfo.getFollowerCount();
        this.followingCount = fUserInfo.getFollowingCount();
        this.backOut = fUserInfo.getBackOut();
        this.lastBackOutTime = fUserInfo.getLastBackOutTime();
        this.selfIntroduction = fUserInfo.getSelfIntroduction();
        this.cumulativeInfluence = fUserInfo.getCumulativeInfluence();
        this.uPoint = fUserInfo.getUPoint();
        this.naPoint = fUserInfo.getNaPoint();
        this.historyOpenAll = fUserInfo.getHistoryOpenAll();
        this.historyOpenFollowSponsor = fUserInfo.getHistoryOpenFollowSponsor();
        this.historyOpenNoOpen = fUserInfo.getHistoryOpenNoOpen();
        this.sponsorHistoryOpenAll = fUserInfo.getSponsorHistoryOpenAll();
        this.sponsorHistoryOpenSponAndFollowFromMe = fUserInfo.getSponsorHistoryOpenSponAndFollowFromMe();
        this.sponsorHistoryOpenSponNoOpen = fUserInfo.getSponsorHistoryOpenSponNoOpen();
        this.alarmChatMessage = fUserInfo.getAlarmChatMessage();
        this.alarmContentReply = fUserInfo.getAlarmContentReply();
        this.alarmReplyAndReply = fUserInfo.getAlarmReplyAndReply();
        this.alarmFollowNewContent = fUserInfo.getAlarmFollowNewContent();
        this.alarmSponNewContent = fUserInfo.getAlarmSponNewContent();
        this.deactivation = fUserInfo.getDeactivation();
        this.maliciousCount = fUserInfo.getMaliciousCount();
        this.stopPeriod = fUserInfo.getStopPeriod();
        this.maliciousCause = fUserInfo.getMaliciousCause();
        this.maliciousMessageCheck = fUserInfo.getMaliciousMessageCheck();

    }
}
