package com.wing.forutona.FTag.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
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
    TagRankingWrapdto getFTagRankingFromBallInfluencePower(
            TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto
    ) throws ParseException;

    TagRankingWrapdto getRelationTagRankingFromTagNameOrderByBallPower(RelationTagRankingFromTagNameReqDto reqDto);

    TagResDtoWrap getTagFromBallUuid(TagFromBallReqDto reqDto);
}

@Service
@RequiredArgsConstructor
class FTagServiceImpl implements FTagService {

    final FBallTagQueryRepository fBallTagQueryRepository;

    final FBallTagDataRepository fBallTagDataRepository;

    final DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Transactional
    public TagRankingWrapdto getFTagRankingFromBallInfluencePower(
            TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto)
            throws ParseException {

        int searchDistance = distanceOfBallCountToLimitService
                .distanceOfBallCountToLimit(tagRankingFromBallInfluencePowerReqDto.getLatitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLongitude(), tagRankingFromBallInfluencePowerReqDto.getLimit());

        Geometry rect = GisGeometryUtil
                .createRect(tagRankingFromBallInfluencePowerReqDto.getLatitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLongitude(), searchDistance);

        Geometry centerPoint = GisGeometryUtil
                .createCenterPoint(tagRankingFromBallInfluencePowerReqDto.getLatitude()
                        , tagRankingFromBallInfluencePowerReqDto.getLongitude());

        return fBallTagQueryRepository.getFindTagRankingInDistanceOfInfluencePower(centerPoint, rect, tagRankingFromBallInfluencePowerReqDto.getLimit());
    }


    @Transactional
    public TagRankingWrapdto getRelationTagRankingFromTagNameOrderByBallPower(RelationTagRankingFromTagNameReqDto reqDto) {
        return fBallTagQueryRepository.getRelationTagRankingFromTagNameOrderByBallPower(reqDto);
    }

    @Transactional
    public TagResDtoWrap getTagFromBallUuid(TagFromBallReqDto reqDto) {
        FBall fBall =  FBall.builder().ballUuid(reqDto.getBallUuid()).build();
        List<FBalltag> fBalltagByBallUuidIs = fBallTagDataRepository.findFBalltagByBallUuidIs(fBall);
        List<TagResDto> collect = fBalltagByBallUuidIs.stream().map(x -> new TagResDto(x)).collect(Collectors.toList());
        TagResDtoWrap dtoWrap = new TagResDtoWrap();
        dtoWrap.setTotalCount(collect.size());
        dtoWrap.setTags(collect);
        return dtoWrap;
    }
}
