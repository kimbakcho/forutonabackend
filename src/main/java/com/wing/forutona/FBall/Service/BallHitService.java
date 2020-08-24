package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BallHitService {
    Long hit(String fBallUuid);
}

@Service
@Transactional
@RequiredArgsConstructor
class BallHitServiceImpl implements  BallHitService {

    final  FBallDataRepository fBallDataRepository;

    @Override
    public Long hit(String fBallUuid) {
        Optional<FBall> byId = fBallDataRepository.findById(fBallUuid);
        byId.get().actionBallHit();
        return byId.get().getBallHits();
    }
}
