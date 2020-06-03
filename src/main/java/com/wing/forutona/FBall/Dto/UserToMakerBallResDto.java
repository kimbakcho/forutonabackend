package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBall;
import lombok.Data;

@Data
public class UserToMakerBallResDto extends FBallResDto{

    @QueryProjection
    public UserToMakerBallResDto(FBall fBall){
        super(fBall);
    }
}
