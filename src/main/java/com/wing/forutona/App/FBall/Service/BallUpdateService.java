package com.wing.forutona.App.FBall.Service;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBall.Dto.FBallUpdateReqDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BallUpdateService {
    FBallResDto updateBall(FBallUpdateReqDto reqDto,String userUid) throws Exception;
}

@Service
@Transactional
@RequiredArgsConstructor
class BallUpdateServiceImpl implements BallUpdateService {

    final FBallDataRepository fBallDataRepository;

    @Override
    public FBallResDto updateBall(FBallUpdateReqDto reqDto,String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (!fBall.getMakerUid().equals(userUid)) {
            throw new Exception("don't Have updateBall Permisstion");
        }
        fBall.setPlacePoint(reqDto.getLongitude(), reqDto.getLatitude());
        fBall.setBallName(reqDto.getBallName());
        fBall.setPlaceAddress(reqDto.getPlaceAddress());
        fBall.setDescription(reqDto.getDescription());
        return new FBallResDto(fBall);
    }
}