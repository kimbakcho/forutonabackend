package com.wing.forutona.FBall.Domain;

import com.querydsl.core.annotations.QueryEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Dto.FBallInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallState;
import com.wing.forutona.FBall.Dto.FBallType;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class FBall {
  @Id
  @Column(unique = true)
  private String ballUuid;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uid")
  private FUserInfo fBallUid;
  private double longitude;
  private double latitude;
  @Column(columnDefinition="geometry(Point,4326)")
  private Point placePoint;
  //ALTER TABLE `Fball` ADD FULLTEXT INDEX `BallNameindex` (`ballName`) WITH PARSER ngram; ngram index 사용
  private String ballName;
  @Enumerated(EnumType.STRING)
  private FBallType ballType;
  private LocalDateTime makeTime;
  @Enumerated(EnumType.STRING)
  private FBallState ballState;
  private String placeAddress;
  private String administrativeArea;
  private String country;
  private double pointReward;
  private double influenceReward;
  private LocalDateTime activationTime;
  private String ballPassword;
  private long hasPassword;
  private long ballHits;
  private long ballLikes;
  private long ballDisLikes;
  private long ballPower;
  private long joinPlayer;
  private long maximumPlayers;
  private double starPoints;
  private long expGiveFlag;
  private double makeExp;
  private long commentCount;
  private double userExp;
  private String description;
  private long contributor;



  @OneToMany(mappedBy = "ballUuid",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
  List<FBalltag> tags;

  public FBall(FBallInsertReqDto reqDto){
    this.ballUuid = reqDto.getBallUuid();
    this.longitude = reqDto.getLongitude();
    this.latitude = reqDto.getLatitude();
    GeometryFactory geomFactory = new GeometryFactory();
    this.placePoint = geomFactory.createPoint(new Coordinate(reqDto.getLongitude(),reqDto.getLatitude()));
    this.placePoint.setSRID(4326);
    this.ballName = reqDto.getBallName();
    this.ballType = reqDto.getBallType();
    this.placeAddress = reqDto.getPlaceAddress();
    this.administrativeArea = reqDto.getAdministrativeArea();
    this.country = reqDto.getCountry();
    this.ballPassword = reqDto.getBallPassword();
    this.maximumPlayers = reqDto.getMaximumPlayers();
    this.description = reqDto.getDescription();
    List<FBalltag> tagCollect = reqDto.getTags().stream().map(x -> new FBalltag(this, x)).collect(Collectors.toList());
    this.tags = tagCollect;
  }

}
