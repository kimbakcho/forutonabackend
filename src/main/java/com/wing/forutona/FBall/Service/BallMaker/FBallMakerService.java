package com.wing.forutona.FBall.Service.BallMaker;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Dto.FBallInsertReqDto;

public interface  FBallMakerService {
    int insertBall(FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken);
    int updateBall(FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken);

}
