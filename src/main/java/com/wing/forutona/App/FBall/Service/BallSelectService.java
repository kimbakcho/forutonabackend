package com.wing.forutona.App.FBall.Service;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBall.Repository.FBallQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface BallSelectService {
    FBallResDto selectBall(String ballUuid);

    List<FBallResDto> selectBalls(List<String> ballUuid);
}

@Service
@Transactional
@RequiredArgsConstructor
class BallSelectServiceImpl implements  BallSelectService {

    final FBallDataRepository fBallDataRepository;

    final FBallQueryRepository fBallQueryRepository;

    @Override
    public FBallResDto selectBall(String ballUuid) {
        return new FBallResDto(fBallDataRepository.findById(ballUuid).get());
    }

    @Override
    public List<FBallResDto> selectBalls(List<String> ballUuid) {
        List<FBall> byBallUuids = fBallQueryRepository.findByBallUuids(ballUuid);
        List<FBallResDto> collect = byBallUuids.stream().map(x -> {
            return new FBallResDto(x);
        }).collect(Collectors.toList());
        return collect;

    }
}
