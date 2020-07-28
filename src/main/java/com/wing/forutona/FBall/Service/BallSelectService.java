package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BallSelectService {
    FBallResDto selectBall(String ballUuid);
}

@Service
@Transactional
@RequiredArgsConstructor
class BallSelectServiceImpl implements  BallSelectService {

    final FBallDataRepository fBallDataRepository;

    @Override
    public FBallResDto selectBall(String ballUuid) {
        return new FBallResDto(fBallDataRepository.findById(ballUuid).get());
    }
}
