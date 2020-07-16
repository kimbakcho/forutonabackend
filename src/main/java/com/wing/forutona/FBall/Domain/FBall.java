package com.wing.forutona.FBall.Domain;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.FBall.Dto.FBallState;
import com.wing.forutona.FBall.Dto.FBallType;
import com.wing.forutona.FBall.Dto.IssueBallInsertReqDto;
import com.wing.forutona.FBall.Dto.IssueBallUpdateReqDto;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FBall {

    @OneToMany(mappedBy = "ballUuid", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<FBalltag> tags = new ArrayList<>();
    @Id
    @Column(unique = true)
    private String ballUuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private FUserInfo uid;
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
    private long ballLikes = 0;
    private long ballDisLikes = 0;
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
    public FBall(String ballUuid, LocalDateTime makeTime, FBallState ballState, FUserInfo uid,
                 double pointReward, double influenceReward, LocalDateTime activationTime,
                 long ballHits, double makeExp) {
        this.ballUuid = ballUuid;
        this.makeTime = makeTime;
        this.ballState = ballState;
        this.uid = uid;
        this.pointReward = pointReward;
        this.influenceReward = influenceReward;
        this.activationTime = activationTime;
        this.ballHits = ballHits;
        this.makeExp = makeExp;
    }


    public static FBall fromIssueBallInsertReqDto(IssueBallInsertReqDto reqDto) {
        FBall fBall = new FBall();
        fBall.ballUuid = reqDto.getBallUuid();
        fBall.longitude = reqDto.getLongitude();
        fBall.latitude = reqDto.getLatitude();
        GeometryFactory geomFactory = new GeometryFactory();
        fBall.placePoint = geomFactory.createPoint(new Coordinate(reqDto.getLongitude(), reqDto.getLatitude()));
        fBall.placePoint.setSRID(4326);
        fBall.ballName = reqDto.getBallName();
        fBall.ballType = reqDto.getBallType();
        fBall.placeAddress = reqDto.getPlaceAddress();
        fBall.description = reqDto.getDescription();
        List<FBalltag> tagCollect = reqDto.getTags().stream()
                .map(x -> FBalltag.builder()
                        .ballUuid(fBall)
                        .tagItem(x.getTagItem())
                        .build()
                ).collect(Collectors.toList());
        fBall.tags = tagCollect;
        fBall.ballDeleteFlag = false;
        return fBall;
    }

    public void setUid(FUserInfo fBallUid) {
        this.uid = fBallUid;
    }

    public void setMakeTime(LocalDateTime makeTime) {
        this.makeTime = makeTime;
    }

    public void setBallState(FBallState ballState) {
        this.ballState = ballState;
    }

    public void setPointReward(long pointReward) {
        this.pointReward = pointReward;
    }

    public void setInfluenceReward(long influenceReward) {
        this.influenceReward = influenceReward;
    }

    public void setActivationTime(LocalDateTime activationTime) {
        this.activationTime = activationTime;
    }

    public void setBallHits(long ballHits) {
        this.ballHits = ballHits;
    }

    public void setJoinPlayer(long joinPlayer) {
        this.joinPlayer = joinPlayer;
    }

    public void setMaximumPlayers(long maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public void setStarPoints(long starPoints) {
        this.starPoints = starPoints;
    }

    public void setExpGiveFlag(long expGiveFlag) {
        this.expGiveFlag = expGiveFlag;
    }

    public void setMakeExp(long makeExp) {
        this.makeExp = makeExp;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public void setUserExp(long userExp) {
        this.userExp = userExp;
    }

    public void setPlacePoint(double longitude, double latitude) {
        GeometryFactory geomFactory = new GeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);
        this.placePoint = point;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void updateIssueBallUpdateReqDto(IssueBallUpdateReqDto reqDto) {
        setPlacePoint(reqDto.getLongitude(), reqDto.getLatitude());
        ballName = reqDto.getBallName();
        placeAddress = reqDto.getPlaceAddress();
        description = reqDto.getDescription();
    }

    public void delete() {
        ballDeleteFlag = true;
        description = "{}";
    }


    public void setContributor(long contributor) {
        this.contributor = contributor;
    }

    public void setBallLikesFromBallValuation(long upAndDown) {
        if (upAndDown > 0) {
            this.ballLikes += Math.abs(upAndDown);
        }
    }

    public void setBallDisLikesFromBallValuation(long upAndDown) {
        if (upAndDown < 0) {
            this.ballDisLikes += Math.abs(upAndDown);
        }
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
}
