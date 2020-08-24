package com.wing.forutona.FBall.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.MapFindScopeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DistanceOfBallCountToLimitService {
    int distanceOfBallCountToLimit(double latitude, double longitude, int limit) throws ParseException;
}

@Service
@RequiredArgsConstructor
class DistanceOfBallCountToLimitServiceImpl implements DistanceOfBallCountToLimitService {

    final private MapFindScopeStepRepository mapFindScopeStepRepository;
    final private FBallQueryRepository fBallQueryRepository;

    @Override
    @Transactional
    public int distanceOfBallCountToLimit(double latitude, double longitude, int limit) throws ParseException {
        List<FMapFindScopeStep> scopeMeter = mapFindScopeStepRepository.findAll(Sort.by("scopeMeter").ascending());
        int currentScopeMater = 0;
        for (FMapFindScopeStep mapFindScopeStep : scopeMeter) {
            currentScopeMater = mapFindScopeStep.getScopeMeter();
            Geometry rect = GisGeometryUtil.createRect(latitude, longitude, currentScopeMater);
            if (fBallQueryRepository.getFindBallCountInDistance(rect) > limit) {
                return currentScopeMater;
            }
        }
        return currentScopeMater;
    }
}
