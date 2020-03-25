package com.wing.forutona.Fcube.Domain;


import com.wing.forutona.ForutonaUser.Domain.JUserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class JFcube {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idx;
  @Column(unique = true)
  private String cubeuuid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uid")
  private JUserInfo Fcubeuid;

  private double longitude;
  private double latitude;
  private long placePoint;
  private String cubeName;
  private String cubeType;
  private LocalDateTime makeTime;
  private double influence;
  private long cubeState;
  private String placeAddress;
  private String administrativeArea;
  private String country;
  private double naPoint;
  private double youPoint;
  private double pointReward;
  private double influenceReward;
  private LocalDateTime activationTime;
  private String cubePassword;
  private long hasPassword;
  private long cubeScope;
  private double influenceLevel;
  private long cubeHits;
  private long cubeLikes;
  private long cubeDisLikes;
  private long ballPower;
  private long joinPlayer;
  private long maximumPlayers;
  private double starPoints;
  private long expGiveFlag;
  private double makeExp;
  private long commentCount;
  private double userExp;
  private String description;




}
