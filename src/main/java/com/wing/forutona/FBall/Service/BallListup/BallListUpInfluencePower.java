package com.wing.forutona.FBall.Service.BallListup;

import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Dto.FBallListUpFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BallListUpInfluencePower implements BallListUpService {

    final FBallQueryRepository fBallQueryRepository;

    final DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Override
    public Page<FBallResDto> search(Object reqDto, FSorts sort, Pageable pageable) throws Exception {
        if (!(reqDto instanceof FBallListUpFromBallInfluencePowerReqDto)) {
            throw new Exception("Not Type FBallListUpFromBallInfluencePowerReqDto");
        }
        FBallListUpFromBallInfluencePowerReqDto acceptReqDto = (FBallListUpFromBallInfluencePowerReqDto) reqDto;

        int findDistanceRangeLimit = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(
                acceptReqDto.getLatitude(),
                acceptReqDto.getLongitude(),
                acceptReqDto.getBallLimit());

        return fBallQueryRepository.getBallListUpFromBallInfluencePower(
                GisGeometryUtil.createCenterPoint(acceptReqDto.getLatitude(), acceptReqDto.getLongitude())
                , GisGeometryUtil.createRect(acceptReqDto.getLatitude(), acceptReqDto.getLongitude(), findDistanceRangeLimit)
                , pageable);
    }
}
