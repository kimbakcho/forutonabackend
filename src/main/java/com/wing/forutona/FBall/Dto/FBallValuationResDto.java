package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class FBallValuationResDto {

    String valueUuid;
    FBallResDto ballUuid;
    FUserInfoSimpleResDto uid;
    Long ballLike = 0L;
    Long ballDislike = 0L;
    Long point = 0L;

    public FBallValuationResDto (FBallValuation fBallValuation){
        this.valueUuid = fBallValuation.getValueUuid();
        this.ballUuid = new FBallResDto(fBallValuation.getBallUuid());
        this.uid = new FUserInfoSimpleResDto(fBallValuation.getUid());
        this.ballLike = fBallValuation.getBallLike();
        this.ballDislike = fBallValuation.getBallDislike();
        this.point = fBallValuation.getPoint();
    }
}
