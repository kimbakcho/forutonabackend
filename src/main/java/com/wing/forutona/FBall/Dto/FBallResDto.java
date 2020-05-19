package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FBallResDto {
    private double latitude;
    private double longitude;
    private String ballUuid;
    private String ballName;
    private FBallType ballType;
    private FBallState ballState;
    private String placeAddress;
    private long ballHits;
    private long ballLikes;
    private long ballDisLikes;
    private long commentCount;
    private long ballPower;
    private LocalDateTime activationTime;
    private LocalDateTime makeTime;
    private String description;
    private String nickName;
    private String profilePicktureUrl;
    private String uid;
    private Double userLevel;
    //해당 부분 BallPower/지도 중심과의 거리로 계산
    private double influencePower;
    private long contributor;
    private boolean ballDeleteFlag;

    @QueryProjection
    public FBallResDto(FBall fball, double influencePower) {
        this.latitude = fball.getLatitude();
        this.longitude = fball.getLongitude();
        this.ballUuid = fball.getBallUuid();
        this.ballName = fball.getBallName();
        this.ballType = fball.getBallType();
        this.ballState = fball.getBallState();
        this.placeAddress = fball.getPlaceAddress();
        this.ballHits = fball.getBallHits();
        this.ballLikes = fball.getBallLikes();
        this.ballDisLikes = fball.getBallDisLikes();
        this.commentCount = fball.getCommentCount();
        this.ballPower = fball.getBallPower();
        this.activationTime = fball.getActivationTime();
        this.makeTime = fball.getMakeTime();
        this.description = fball.getDescription();
        this.nickName = fball.getFBallUid().getNickName();
        this.profilePicktureUrl = fball.getFBallUid().getProfilePictureUrl();
        this.uid = fball.getFBallUid().getUid();
        this.userLevel = fball.getFBallUid().getUserLevel();
        this.influencePower = influencePower;
        this.contributor = fball.getContributor();
        this.ballDeleteFlag = fball.isBallDeleteFlag();
    }

    @QueryProjection
    public FBallResDto(FBall fball) {
        this.latitude = fball.getLatitude();
        this.longitude = fball.getLongitude();
        this.ballUuid = fball.getBallUuid();
        this.ballName = fball.getBallName();
        this.ballType = fball.getBallType();
        this.ballState = fball.getBallState();
        this.placeAddress = fball.getPlaceAddress();
        this.ballHits = fball.getBallHits();
        this.ballLikes = fball.getBallLikes();
        this.ballDisLikes = fball.getBallDisLikes();
        this.commentCount = fball.getCommentCount();
        this.ballPower = fball.getBallPower();
        this.activationTime = fball.getActivationTime();
        this.makeTime = fball.getMakeTime();
        this.description = fball.getDescription();
        this.nickName = fball.getFBallUid().getNickName();
        this.profilePicktureUrl = fball.getFBallUid().getProfilePictureUrl();
        this.uid = fball.getFBallUid().getUid();
        this.userLevel = fball.getFBallUid().getUserLevel();
        this.contributor = fball.getContributor();
        this.ballDeleteFlag = fball.isBallDeleteFlag();
    }

}
