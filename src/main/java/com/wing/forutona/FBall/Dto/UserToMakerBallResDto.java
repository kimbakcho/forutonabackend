package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import lombok.Data;

@Data
public class UserToMakerBallResDto extends UserBallResDto{

    @QueryProjection
    public UserToMakerBallResDto(FBall fBall){
        fballResDto = new FBallResDto(fBall);
    }
}
