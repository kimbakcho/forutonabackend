package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Dto.FBallUpdateReqDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FTag.Domain.FBalltag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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