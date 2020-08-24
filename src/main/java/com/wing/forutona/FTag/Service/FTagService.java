package com.wing.forutona.FTag.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.*;
import com.wing.forutona.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface FTagService {
    List<TagRankingResDto> getFTagRankingFromBallInfluencePower(
            TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto
    ) throws ParseException;

    List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag);

    List<FBallTagResDto> getTagFromBallUuid(String ballUuid);
}

@Service
@RequiredArgsConstructor
@Transactional
class FTagServiceImpl implements FTagService {

    final FBallTagQueryRepository fBallTagQueryRepository;

    final FBallTagDataRepository fBallTagDataRepository;

    final FBallDataRepository fBallDataRepository;

    final DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    public List<TagRankingResDto> getFTagRankingFromBallInfluencePower(
            TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto)
            throws ParseException {

        int searchDistance = distanceOfBallCountToLimitService
                .distanceOfBallCountToLimit(tagRankingFromBallInfluencePowerReqDto.getLatitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLongitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLimit());

        Geometry rect = GisGeometryUtil
                .createRect(tagRankingFromBallInfluencePowerReqDto.getLatitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLongitude(), searchDistance);

        Geometry centerPoint = GisGeometryUtil
                .createCenterPoint(tagRankingFromBallInfluencePowerReqDto.getLatitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLongitude());

        return fBallTagQueryRepository.getFindTagRankingInDistanceOfInfluencePower(centerPoint, rect, tagRankingFromBallInfluencePowerReqDto.getLimit());
    }

    public List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag) {
        return fBallTagQueryRepository.getRelationTagRankingFromTagNameOrderByBallPower(searchTag);
    }

    public List<FBallTagResDto> getTagFromBallUuid(String ballUuid) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        List<FBalltag> fBallTags = fBallTagDataRepository.findByBallUuid(fBall);
        List<FBallTagResDto> collect = fBallTags.stream().map(x -> new FBallTagResDto(x)).collect(Collectors.toList());
        return collect;
    }
}
