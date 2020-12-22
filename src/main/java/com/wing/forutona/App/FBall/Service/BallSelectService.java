package com.wing.forutona.App.FBall.Service;

import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
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
