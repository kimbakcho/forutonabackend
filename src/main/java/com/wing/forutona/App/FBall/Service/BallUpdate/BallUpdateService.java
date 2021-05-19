package com.wing.forutona.App.FBall.Service.BallUpdate;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBall.Dto.FBallUpdateReqDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FTag.Domain.FBalltag;
import com.wing.forutona.App.FTag.Repository.FBallTagDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BallUpdateService {
    FBallResDto updateBall(FBallUpdateReqDto reqDto,String userUid) throws Exception;
}
