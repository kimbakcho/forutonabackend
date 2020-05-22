package com.wing.forutona.FBall.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class UserBallResDto {
    String fBallUuid;
    FBallType fBallType;
    double longitude;
    double latitude;
    String ballName;
    String ballPlaceAddress;
    //ball의 Like
    long ballLikes;
    //ball의 DisLike
    long ballDisLikes;
    long commentCount;
    LocalDateTime activationTime;
    LocalDateTime makeTime;
    boolean ballDeleteFlag;
    String ballUid;

}
