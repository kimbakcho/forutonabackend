package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FBall.Domain.FBallValuation;
import lombok.Data;

@Data
public class FBallValuationResDto {
    private Long idx;
    private String ballUuid;
    private String uid;
    private Long upAndDown;


    public FBallValuationResDto(FBallValuation fBallValuation) {
        this.idx = fBallValuation.getIdx();
        this.ballUuid = fBallValuation.getBallUuid().getBallUuid();
        this.uid = fBallValuation.getUid().getUid();
        this.upAndDown = fBallValuation.getUpAndDown();

    }
}
