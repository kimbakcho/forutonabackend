package com.wing.forutona.App.ForutonaUser.Domain;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoJoinReqDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "UserInfo")
public class FUserInfo {
    @Id
    @Column(unique = true)
    private String uid;
    private String nickName;
    private String profilePictureUrl;
    private String backGroundImageUrl;
    @Enumerated(EnumType.STRING)
    private GenderType gender;
    private LocalDate ageDate;
    private String email;
    @ColumnDefault("0")
    private Boolean forutonaAgree = false;
    @ColumnDefault("0")
    private Boolean forutonaManagementAgree = false;
    @ColumnDefault("0")
    private Boolean privateAgree = false;
    @ColumnDefault("0")
    private Boolean positionAgree = false;
    @ColumnDefault("0")
    private Boolean martketingAgree = false;
    @ColumnDefault("0")
    private Boolean ageLimitAgree = false;
    private String snsService;
    private String phoneNumber;
    private String isoCode;
    @ColumnDefault("37.4402052")
    private Double latitude;
    @ColumnDefault("126.79369789999998")
    private Double longitude;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point placePoint;
    private LocalDateTime positionUpdateTime;
    @ColumnDefault("0")
    private Double userLevel = 0.0;
    @ColumnDefault("0")
    private Double expPoint = 0.0;
    private String fCMtoken;
    @Column(name = "joinTime", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joinTime;
    @ColumnDefault("0")
    private Long followerCount = 0L;
    private Long followingCount = 0L;
    @ColumnDefault("0")
    private Long backOut = 0L;
    private LocalDateTime lastBackOutTime;
    private String selfIntroduction;
    @ColumnDefault("0")
    private Double cumulativeInfluence =  0.0;
    @ColumnDefault("0")
    private Double uPoint = 0.0;
    @ColumnDefault("0")
    private Double naPoint = 0.0;
    @ColumnDefault("1")
    private Long historyOpenAll = 1L;
    @ColumnDefault("0")
    private Long historyOpenFollowSponsor = 0L;
    @ColumnDefault("0")
    private Long historyOpenNoOpen = 0L;
    @ColumnDefault("0")
    private Long sponsorHistoryOpenAll = 0L;
    @ColumnDefault("0")
    private Long sponsorHistoryOpenSponAndFollowFromMe = 0L;
    @ColumnDefault("1")
    private Long sponsorHistoryOpenSponNoOpen = 1L;
    @ColumnDefault("1")
    private Boolean alarmChatMessage = true;
    @ColumnDefault("1")
    private Boolean alarmContentReply =true;
    @ColumnDefault("1")
    private Boolean alarmReplyAndReply = true;
    @ColumnDefault("1")
    private Boolean alarmFollowNewContent =true;
    @ColumnDefault("0")
    private Boolean alarmSponNewContent =false;
    @ColumnDefault("1")
    private Long deactivation =1L;
    @ColumnDefault("0")
    private Double playerPower = 0.0;
    int maliciousCount ;
    LocalDateTime stopPeriod;
    Boolean maliciousMessageCheck = false;
    String maliciousCause;

    @Builder
    public FUserInfo(String uid, String fCMtoken, String nickName, double longitude, double latitude) {
        this.uid = uid;
        this.fCMtoken = fCMtoken;
        this.nickName = nickName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.updatePlacePoint(this.latitude, this.longitude);
        this.maliciousCount = 0;
        this.maliciousMessageCheck = false;
    }

    public static FUserInfo fromFUserInfoJoinReqDto(FUserInfoJoinReqDto reqDto) {
        FUserInfo fUserInfo = new FUserInfo();
        fUserInfo.uid = reqDto.getEmailUserUid();
        fUserInfo.snsService = reqDto.getSnsSupportService().name();
        fUserInfo.forutonaAgree = reqDto.isForutonaAgree();
        fUserInfo.forutonaManagementAgree = reqDto.isForutonaManagementAgree();
        fUserInfo.privateAgree = reqDto.isPrivateAgree();
        fUserInfo.positionAgree = reqDto.isPositionAgree();
        fUserInfo.martketingAgree = reqDto.isMartketingAgree();
        fUserInfo.ageLimitAgree = reqDto.isAgeLimitAgree();
        fUserInfo.email = reqDto.getEmail();
        fUserInfo.isoCode = reqDto.getCountryCode();
        fUserInfo.selfIntroduction = reqDto.getUserIntroduce();
        fUserInfo.nickName = reqDto.getNickName();
        fUserInfo.latitude = 37.4402052;
        fUserInfo.longitude = 126.79369789999998;
        GeometryFactory geomFactory = new GeometryFactory();
        Point placePoint = geomFactory.createPoint(new Coordinate(fUserInfo.longitude, fUserInfo.latitude));
        placePoint.setSRID(4326);
        fUserInfo.placePoint = placePoint;
        fUserInfo.phoneNumber = reqDto.getInternationalizedPhoneNumber();
        fUserInfo.gender = reqDto.getGender();
        fUserInfo.ageDate = reqDto.getAgeDate().toLocalDate();
        fUserInfo.maliciousCount = 0;
        fUserInfo.maliciousMessageCheck = false;
        return fUserInfo;
    }

    public void updatePlacePoint(double latitude, double longitude) {
        GeometryFactory geomFactory = new GeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);
        this.placePoint = point;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void updateCumulativeInfluence(double upAndDown) {
        this.cumulativeInfluence += upAndDown;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setBackGroundImageUrl(String backGroundImageUrl) {
        this.backGroundImageUrl = backGroundImageUrl;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public void setFCMtoken(String fCMtoken) {
        this.fCMtoken = fCMtoken;
    }

    public void setMaliciousMessageCheck(boolean maliciousMessageCheck) {
        this.maliciousMessageCheck = maliciousMessageCheck;
    }

    public void setAlarmChatMessage(Boolean alarmChatMessage) {
        this.alarmChatMessage = alarmChatMessage;
    }

    public void setAlarmContentReply(Boolean alarmContentReply) {
        this.alarmContentReply = alarmContentReply;
    }

    public void setAlarmReplyAndReply(Boolean alarmReplyAndReply) {
        this.alarmReplyAndReply = alarmReplyAndReply;
    }

    public void setAlarmFollowNewContent(Boolean alarmFollowNewContent) {
        this.alarmFollowNewContent = alarmFollowNewContent;
    }

    public void setAlarmSponNewContent(Boolean alarmSponNewContent) {
        this.alarmSponNewContent = alarmSponNewContent;
    }
}
