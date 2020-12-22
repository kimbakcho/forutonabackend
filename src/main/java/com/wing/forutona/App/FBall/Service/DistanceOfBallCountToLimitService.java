package com.wing.forutona.App.FBall.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.App.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.App.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.App.FBall.Repository.MapFindScopeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DistanceOfBallCountToLimitService {
    int distanceOfBallCountToLimit(LatLng centerPoint) throws ParseException;
}

@Service
@RequiredArgsConstructor
class DistanceOfBallCountToLimitServiceImpl implements DistanceOfBallCountToLimitService {

    final private MapFindScopeStepRepository mapFindScopeStepRepository;
    final private FBallQueryRepository fBallQueryRepository;
    final static int limitFindBallCountPolicy = 1000;

    @Override
    @Transactional
    public int distanceOfBallCountToLimit(LatLng centerPoint) throws ParseException {
        List<FMapFindScopeStep> scopeMeter = mapFindScopeStepRepository.findAll(Sort.by("scopeMeter").ascending());
        int currentScopeMater = 0;
        for (FMapFindScopeStep mapFindScopeStep : scopeMeter) {
            currentScopeMater = mapFindScopeStep.getScopeMeter();
            if (fBallQueryRepository.findByCountIsCriteriaBallFromDistance(centerPoint,currentScopeMater) >  limitFindBallCountPolicy) {
                return currentScopeMater;
            }
        }
        return currentScopeMater;
    }
}
