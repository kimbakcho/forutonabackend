package com.wing.forutona.ForutonaUser.Domain;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoJoinReqDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
  private long gender;
  private LocalDate ageDate;
  private String email;
  private Boolean forutonaAgree;
  private Boolean forutonaManagementAgree;
  private Boolean privateAgree;
  private Boolean positionAgree;
  private Boolean martketingAgree;
  private Boolean ageLimitAgree;
  private String snsService;
  private String phoneNumber;
  private String isoCode;
  private Double latitude;
  private Double longitude;
  @Column(columnDefinition="geometry(Point,4326)")
  private Point placePoint;
  private LocalDateTime positionUpdateTime;
  private Double userLevel;
  private Double expPoint;
  private String fCMtoken;
  private LocalDateTime joinTime;
  private Long followCount;
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
  private Long alarmChatMessage;
  private Long alarmContentReply;
  private Long alarmReplyAndReply;
  private Long alarmFollowNewContent;
  private Long alarmSponNewContent;
  private Long deactivation;

  @OneToMany(mappedBy = "uid")
  private List<FBall> userBalls = new ArrayList<>();

  public void addBall(FBall fBall){
    this.userBalls.add(fBall);
    fBall.setUid(this);
  }

  @Builder
  public FUserInfo(String uid,String fCMtoken,String nickName){
    this.uid = uid;
    this.fCMtoken = fCMtoken;
    this.nickName = nickName;
  }

  public static FUserInfo fromFUserInfoJoinReqDto (FUserInfoJoinReqDto reqDto){
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
    fUserInfo.profilePictureUrl = reqDto.getUserProfileImageUrl();
    GeometryFactory geomFactory = new GeometryFactory();
    Point placePoint = geomFactory.createPoint(new Coordinate(126.79369789999998, 37.4402052));
    placePoint.setSRID(4326);
    fUserInfo.placePoint = placePoint;
    fUserInfo.phoneNumber = reqDto.getInternationalizedPhoneNumber();
    return fUserInfo;
  }

  public void updatePlacePoint(double latitude,double longitude){
    GeometryFactory geomFactory = new GeometryFactory();
    Point point = geomFactory.createPoint(new Coordinate(longitude,latitude));
    point.setSRID(4326);
    this.placePoint = point;
    this.latitude = latitude;
    this.longitude = longitude;
  }


  public void updateCumulativeInfluence(double upAndDown) {
    this.cumulativeInfluence += upAndDown;
  }

  public void setProfilePictureUrl(String profilePictureUrl) {
    this.profilePictureUrl =profilePictureUrl;
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


}
