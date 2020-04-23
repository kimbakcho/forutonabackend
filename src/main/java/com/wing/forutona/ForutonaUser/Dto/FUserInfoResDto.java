package com.wing.forutona.ForutonaUser.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
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
    private long gender;
    private LocalDate ageDate;
    private String email;
    private long forutonaAgree;
    private long privateAgree;
    private long positionAgree;
    private long martketingAgree;
    private long ageLimitAgree;
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
    private String selfIntroduction;
    private double cumulativeInfluence;
    private double uPoint;
    private double naPoint;
    private long historyOpenAll;
    private long historyOpenFollowSponsor;
    private long historyOpenNoOpen;
    private long sponsorHistoryOpenAll;
    private long sponsorHistoryOpenSponAndFollowFromMe;
    private long sponsorHistoryOpenSponNoOpen;
    private long alarmChatMessage;
    private long alarmContentReply;
    private long alarmReplyAndReply;
    private long alarmFollowNewContent;
    private long alarmSponNewContent;
    private long deactivation;

    @QueryProjection
    public FUserInfoResDto(FUserInfo fUserInfo){
        this.uid = fUserInfo.getUid();
        this.nickName = fUserInfo.getNickName();
        this.profilePictureUrl = fUserInfo.getProfilePictureUrl();
        this.gender  = fUserInfo.getGender();
        this.ageDate = fUserInfo.getAgeDate();
        this.email = fUserInfo.getEmail();
        this.forutonaAgree = fUserInfo.getForutonaAgree();
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
        this.followCount = fUserInfo.getFollowCount();
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
        this.alarmSponNewContent = fUserInfo.getAlarmFollowNewContent();
        this.deactivation = fUserInfo.getDeactivation();
    }
}
