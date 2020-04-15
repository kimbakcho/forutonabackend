package com.wing.forutona.ForutonaUser.Domain;


import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.FBall.Domain.FBall;
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
}
