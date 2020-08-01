package com.wing.forutona.FBall.Domain;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FBall {
    @Id
    @Column(unique = true)
    private String ballUuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private FUserInfoSimple uid;
    private double longitude;
    private double latitude;
    @Column(columnDefinition = "geometry(Point,4326)")
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
    private double pointReward = 0;
    private double influenceReward = 0;
    private LocalDateTime activationTime;
    private String ballPassword;
    private long hasPassword;
    private long ballHits = 0;
    private Integer ballLikes = 0;
    private Integer ballDisLikes = 0;
    private long ballPower = 0;
    private long joinPlayer = 0;
    private long maximumPlayers = -1;
    private double starPoints = 0;
    private long expGiveFlag = 0;
    private double makeExp;
    private long commentCount = 0;
    private double userExp = 0;

    private String description;
    private long contributor;
    private boolean ballDeleteFlag;

    @Builder
    public FBall(String ballUuid, LocalDateTime makeTime, FBallState ballState, FUserInfoSimple uid,
                 double longitude,double latitude,Point placePoint,String ballName,FBallType ballType,
                 String placeAddress,String description,
                 double pointReward, double influenceReward, LocalDateTime activationTime,
                 long ballHits, double makeExp) {
        this.ballUuid = ballUuid;
        this.makeTime = makeTime;
        this.ballState = ballState;
        this.longitude = longitude;
        this.latitude = latitude;
        this.placePoint = placePoint;
        this.ballName = ballName;
        this.ballType = ballType;
        this.placeAddress = placeAddress;
        this.description = description;
        this.uid = uid;
        this.pointReward = pointReward;
        this.influenceReward = influenceReward;
        this.activationTime = activationTime;
        this.ballHits = ballHits;
        this.makeExp = makeExp;
    }

    public void setUid(FUserInfoSimple fBallUid) {
        this.uid = fBallUid;
    }

    public void setBallName(String ballName) {
        this.ballName = ballName;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public void setActivationTime(LocalDateTime activationTime) {
        this.activationTime = activationTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlacePoint(double longitude, double latitude) {
        GeometryFactory geomFactory = new GeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);
        this.placePoint = point;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public void delete() {
        ballDeleteFlag = true;
        description = "{}";
    }

    public void actionBallHit(){
        this.ballHits++;
    }

    public void setContributor(long contributor) {
        this.contributor = contributor;
    }

    public Integer plusBallLike(Integer point) {
        this.ballLikes += point;
        return ballDisLikes;
    }

    public Integer plusBallDisLike(Integer point) {
        this.ballDisLikes += point;
        return this.ballDisLikes;
    }
    public void minusBallLike(Integer point) {
        this.ballLikes -= point;
    }
    public void minusBallDisLike(Integer point) {
        this.ballDisLikes -= point;
    }
    public void updateBallPower() {
        this.ballPower = this.ballLikes - this.ballDisLikes;
    }

    public void setBallPower(int ballPower) {
        this.ballPower = ballPower;
    }

    public long addBallReplyCount(){
        commentCount++;
        return commentCount;
    }

    public String getMakerNickName() {
        return this.uid.getNickName();
    }

    public String getMakerProfileImage() {
        return this.uid.getProfilePictureUrl();
    }

    public String getMakerUid(){
        return this.uid.getUid();
    }


}
