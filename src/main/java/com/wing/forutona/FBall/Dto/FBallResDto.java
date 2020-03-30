package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FBallResDto {
    private double latitude;
    private double longitude;
    private String ballUuid;
    private String ballName;
    private FBallState ballState;
    private String placeAddress;
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

    @QueryProjection
    public FBallResDto(double latitude, double longitude, String ballUuid, String ballName, FBallState ballState, String placeAddress,
                       long ballLikes, long ballDisLikes, long commentCount, long ballPower, LocalDateTime activationTime, LocalDateTime makeTime,
                       String description, String nickName, String profilePicktureUrl, String uid, Double userLevel, double influencePower) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ballUuid = ballUuid;
        this.ballName = ballName;
        this.ballState = ballState;
        this.placeAddress = placeAddress;
        this.ballLikes = ballLikes;
        this.ballDisLikes = ballDisLikes;
        this.commentCount = commentCount;
        this.ballPower = ballPower;
        this.activationTime = activationTime;
        this.makeTime = makeTime;
        this.description = description;
        this.nickName = nickName;
        this.profilePicktureUrl = profilePicktureUrl;
        this.uid = uid;
        this.userLevel = userLevel;
        this.influencePower = influencePower;
    }
}
