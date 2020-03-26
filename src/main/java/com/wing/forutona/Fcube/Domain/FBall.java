package com.wing.forutona.Fcube.Domain;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FBall {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idx;
  @Column(unique = true)
  private String ballUuid;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uid")
  private FUserInfo fBallUid;
  private double longitude;
  private double latitude;
  private long placePoint;
  private String ballName;
  private String ballType;
  private LocalDateTime makeTime;
  private double influence;
  private long ballState;
  private String placeAddress;
  private String administrativeArea;
  private String country;
  private double naPoint;
  private double youPoint;
  private double pointReward;
  private double influenceReward;
  private LocalDateTime activationTime;
  private String ballPassword;
  private long hasPassword;
  private long ballScope;
  private double influenceLevel;
  private long ballHits;
  private long ballLikes;
  private long ballDisLikes;
  private long ballPower;
  private long joinPlayer;
  private long maximumPlayers;
  private double starPoints;
  private long expGiveFlag;
  private double makeExp;
  private long ccmmentCount;
  private double userExp;
  private String description;




}
