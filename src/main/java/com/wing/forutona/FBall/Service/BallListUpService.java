package com.wing.forutona.FBall.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public interface BallListUpService {
    Page<FBallResDto> searchBallListUpFromMapArea(BallFromMapAreaReqDto reqDto, Pageable pageable) throws ParseException;

    Page<FBallResDto> searchBallListUpFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto, Pageable pageable) throws ParseException;

    Page<FBallResDto> searchBallListUpFromTagName(FBallListUpFromTagReqDto reqDto, Pageable pageable) throws ParseException;

    Page<FBallResDto> searchBallListUpOrderByBI(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws ParseException;

    Page<FBallResDto> searchBallListUpUserMakerBall(String ballUuid,Pageable pageable) throws ParseException;
}

@Service
@Transactional
@RequiredArgsConstructor
class BallListUpServiceImpl implements BallListUpService {

    final FBallQueryRepository fBallQueryRepository;
    final DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;
    final BallOfInfluenceCalc ballOfInfluenceCalc;

    @Override
    public Page<FBallResDto> searchBallListUpFromMapArea(BallFromMapAreaReqDto reqDto, Pageable pageable) throws ParseException {
        return fBallQueryRepository.getBallListUpFromMapArea((BallFromMapAreaReqDto) reqDto, pageable);
    }

    @Override
    public Page<FBallResDto> searchBallListUpFromSearchTitle(FBallListUpFromSearchTitleReqDto reqDto, Pageable pageable) throws ParseException {
        return fBallQueryRepository.getBallListUpFromSearchTitle((FBallListUpFromSearchTitleReqDto) reqDto, pageable);
    }

    @Override
    public Page<FBallResDto> searchBallListUpFromTagName(FBallListUpFromTagReqDto reqDto, Pageable pageable) throws ParseException {
        return fBallQueryRepository.ListUpFromTagName((FBallListUpFromTagReqDto) reqDto, pageable);
    }

    @Override
    public Page<FBallResDto> searchBallListUpOrderByBI(FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws ParseException {
        LatLng mapCenter = LatLng.newBuilder().setLatitude(reqDto.getMapCenterLatitude()).setLongitude(reqDto.getMapCenterLongitude()).build();
        int findDistanceRangeLimit = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(mapCenter );

        List<FBall> byCriteriaBallFromDistance = fBallQueryRepository.findByCriteriaBallFromDistance(mapCenter, findDistanceRangeLimit);
        LatLng userPosition = LatLng.newBuilder().setLatitude(reqDto.getUserLatitude()).setLongitude(reqDto.getUserLongitude()).build();
        List<FBall> calcBIBalls = ballOfInfluenceCalc.calc(byCriteriaBallFromDistance, userPosition);
        List<FBallResDto> resultBalls =
                calcBIBalls.stream().sorted(Comparator.comparingDouble(FBall::getBI).reversed())
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .map(x -> new FBallResDto(x))
                        .collect(Collectors.toList());
        Page<FBallResDto> pageResult = new PageImpl<FBallResDto>(resultBalls,pageable,calcBIBalls.size());
        return pageResult;
    }

    @Override
    public Page<FBallResDto> searchBallListUpUserMakerBall(String ballUuid, Pageable pageable) throws ParseException {
        return fBallQueryRepository.getUserToMakerBalls(ballUuid, pageable);
    }


}