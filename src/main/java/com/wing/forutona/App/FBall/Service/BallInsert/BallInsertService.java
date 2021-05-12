package com.wing.forutona.App.FBall.Service.BallInsert;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.App.FBall.Dto.FBallInsertReqDto;
import com.wing.forutona.App.FBall.Dto.FBallResDto;

public interface BallInsertService {
    FBallResDto insertBall(FBallInsertReqDto reqDto, String userUid) throws ParseException;
}
