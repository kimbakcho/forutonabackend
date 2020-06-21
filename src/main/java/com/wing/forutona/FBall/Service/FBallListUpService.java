package com.wing.forutona.FBall.Service;

import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Dto.FBallListUpFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface FBallListUpService {
    FBallListUpWrapDto ListUpBallInfluencePower(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws ParseException;
}

@Service
@RequiredArgsConstructor
class FBallListUpServiceImpl implements FBallListUpService {

    final FBallQueryRepository fBallQueryRepository;
    final DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Override
    public FBallListUpWrapDto ListUpBallInfluencePower(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws ParseException {

        int findDistanceRangeLimit = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getBallLimit());

        return fBallQueryRepository.getBallListUpFromBallInfluencePower(
                GisGeometryUtil.createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude())
                , GisGeometryUtil.createRect(reqDto.getLatitude(), reqDto.getLongitude(), findDistanceRangeLimit)
                , pageable);
    }

}
