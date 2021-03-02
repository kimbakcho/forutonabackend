package com.wing.forutona.App.FBallValuation.Dto;

import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoSimpleResDto;
import lombok.Data;

@Data
public class FBallValuationResDto {

    String valueUuid;
    FBallResDto ballUuid;
    Integer ballLike = 0;
    Integer ballDislike = 0;
    Integer point = 0;

    public FBallValuationResDto(){

    }

    public FBallValuationResDto (FBallValuation fBallValuation){
        this.valueUuid = fBallValuation.getValueUuid();
        this.ballUuid = new FBallResDto(fBallValuation.getBallUuid());

        this.ballLike = fBallValuation.getBallLike();
        this.ballDislike = fBallValuation.getBallDislike();
        this.point = fBallValuation.getPoint();
    }
}
