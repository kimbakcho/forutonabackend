package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FBallResDto implements Cloneable {
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
    private FUserInfoSimpleResDto uid;
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
        this.uid = new FUserInfoSimpleResDto(fball.getUid());
        this.influencePower = influencePower;
        this.contributor = fball.getContributor();
        this.ballDeleteFlag = fball.getBallDeleteFlag();
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
        this.uid = new FUserInfoSimpleResDto(fball.getUid());
        this.contributor = fball.getContributor();
        this.ballDeleteFlag = fball.getBallDeleteFlag();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
