package com.wing.forutona.ForutonaUser.Domain;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoJoinReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
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
  private boolean forutonaAgree;
  private boolean forutonaManagementAgree;
  private boolean privateAgree;
  private boolean positionAgree;
  private boolean martketingAgree;
  private boolean ageLimitAgree;
  private String snsService;
  private String phoneNumber;
  private String isoCode;
  private double latitude;
  private double longitude;
  @Column(columnDefinition="geometry(Point,4326)")
  private Point placePoint;
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


  @OneToMany(mappedBy = "fBallUid")
  private List<FBall> userBalls = new ArrayList<>();

  public void addBall(FBall fBall){
    this.userBalls.add(fBall);
    fBall.setFBallUid(this);
  }
  public FUserInfo (FUserInfoJoinReqDto reqDto){
    this.forutonaAgree = reqDto.isForutonaAgree();
    this.forutonaManagementAgree = reqDto.isForutonaManagementAgree();
    this.privateAgree = reqDto.isPrivateAgree();
    this.positionAgree = reqDto.isPositionAgree();
    this.martketingAgree = reqDto.isMartketingAgree();
    this.ageLimitAgree = reqDto.isAgeLimitAgree();
    this.email = reqDto.getEmail();
    this.isoCode = reqDto.getCountryCode();
    this.selfIntroduction = reqDto.getUserIntroduce();
    this.nickName = reqDto.getNickName();
    this.profilePictureUrl = reqDto.getUserProfileImageUrl();
    GeometryFactory geomFactory = new GeometryFactory();
    this.placePoint = geomFactory.createPoint(new Coordinate(37.4402052,126.79369789999998));
    this.placePoint.setSRID(4326);
    this.phoneNumber = reqDto.getInternationalizedPhoneNumber();
  }
}
