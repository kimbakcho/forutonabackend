package com.wing.forutona.FBall.Service.BallMaker;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Dto.FBallInsertReqDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface  FBallMakerService {
    int insertBall(FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken);
    int updateBall(FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken);

}
