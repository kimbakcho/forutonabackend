package com.wing.forutona.ForutonaUser.Domain;

import com.wing.forutona.Fcube.Domain.FBall;
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
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idx;
  @Column(unique = true)
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

  @OneToMany(mappedBy = "fBallUid")
  private List<FBall> userBalls = new ArrayList<>();

  public void addBall(FBall fBall){
    this.userBalls.add(fBall);
    fBall.setFBallUid(this);
  }
}
