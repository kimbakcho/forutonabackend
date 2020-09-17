package com.wing.forutona.FBall.Domain;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    private FUserInfo uid;
    private Double longitude;
    private Double latitude;
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
    @ColumnDefault("0")
    private Double pointReward = 0.0;
    @ColumnDefault("0")
    private Double influenceReward = 0.0;
    private LocalDateTime activationTime;
    @ColumnDefault("")
    private String ballPassword ;
    @ColumnDefault("0")
    private Long hasPassword = 0L;
    @ColumnDefault("0")
    private Long ballHits = 0L;
    @ColumnDefault("0")
    private Long ballLikes = 0L;
    @ColumnDefault("0")
    private Long ballDisLikes = 0L;
    @ColumnDefault("0")
    private Long ballPower = 0L;
    @ColumnDefault("0")
    private Long joinPlayer = 0L;
    private Long maximumPlayers = -1L;
    @ColumnDefault("0")
    private Double starPoints = 0.0;
    @ColumnDefault("0")
    private Long expGiveFlag = 0L;
    @ColumnDefault("0")
    private Double makeExp = 0.0;
    @ColumnDefault("0")
    private Long commentCount = 0L;
    @ColumnDefault("0")
    private Double userExp = 0.0;

    private String description;
    @ColumnDefault("0")
    private Long contributor = 0L;
    @ColumnDefault("0")
    private Boolean ballDeleteFlag = false;



    @Transient
    private Double BI = 0.0;


    @Builder
    public FBall(String ballUuid, LocalDateTime makeTime, FBallState ballState, FUserInfo uid,
                 Double longitude,Double latitude,String ballName,FBallType ballType,
                 String placeAddress,String description, Long ballPower,
                 double pointReward, double influenceReward, LocalDateTime activationTime,
                 long ballHits, double makeExp) {
        this.ballUuid = ballUuid;
        this.makeTime = makeTime;
        this.ballState = ballState;
        this.longitude = longitude;
        this.latitude = latitude;
        this.setPlacePoint(this.longitude,this.latitude);
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
        this.ballPower = ballPower;
    }

    public void setUid(FUserInfo fBallUid) {
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

    public Long plusBallLike(Long point) {
        this.ballLikes += point;
        return ballLikes;
    }

    public Long plusBallDisLike(Long point) {
        this.ballDisLikes += point;
        return this.ballDisLikes;
    }

    public void minusBallLike(Long point) {
        this.ballLikes -= point;
    }

    public void minusBallDisLike(Long point) {
        this.ballDisLikes -= point;
    }

    public void updateBallPower() {
        this.ballPower = this.ballLikes - this.ballDisLikes;
    }

    public void setBallPower(Long ballPower) {
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

    public void setBI(Double BI) {
        this.BI = BI;
    }
}
