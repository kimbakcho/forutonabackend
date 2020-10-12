package com.wing.forutona.FTag.Service;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.FBall.Service.BallOfInfluenceCalc;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.FBallTagResDto;
import com.wing.forutona.FTag.Dto.TagRankingFromTextReqDto;
import com.wing.forutona.FTag.Dto.TagRankingResDto;
import com.wing.forutona.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface FTagService {
    List<TagRankingResDto> getFTagRankingFromBallInfluencePower(LatLng findPosition, int limit) throws ParseException;

    List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag);

    List<FBallTagResDto> getTagFromBallUuid(String ballUuid);

    List<TagRankingResDto> getTagRankingFromTextOrderBySumBI(TagRankingFromTextReqDto tagRankingFromTextReqDto) throws ParseException;
}

@Service
@RequiredArgsConstructor
@Transactional
class FTagServiceImpl implements FTagService {

    final FBallTagQueryRepository fBallTagQueryRepository;

    final FBallTagDataRepository fBallTagDataRepository;

    final FBallDataRepository fBallDataRepository;

    final FBallQueryRepository fBallQueryRepository;

    final DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    final BallOfInfluenceCalc ballOfInfluenceCalc;

    public List<TagRankingResDto> getFTagRankingFromBallInfluencePower(LatLng findPosition, int limit) throws ParseException {
        int searchDistance = distanceOfBallCountToLimitService.distanceOfBallCountToLimit(findPosition);
        List<FBall> sampleBall = fBallQueryRepository.findByCriteriaBallFromDistance(findPosition, searchDistance);
        List<FBalltag> byBallInTags = fBallTagQueryRepository.findByBallInTags(sampleBall);
        return getListOrderByTagPower(findPosition, limit, byBallInTags);
    }

    public List<TagRankingResDto> getListOrderByTagPower(LatLng findPosition, int limit, List<FBalltag> byBallInTags) {
        List<FBalltag> calcTagInBallBI = calcTagRelationBallBI(findPosition, byBallInTags);
        Map<String, Double> groupByTagNameSumBI =
                calcTagInBallBI
                        .stream()
                        .collect(Collectors.groupingBy(x -> x.getTagItem(),
                                Collectors.summingDouble(x -> x.getBallUuid().getBI())));

        List<TagRankingResDto> tagRankingResDtos = new ArrayList<>();
        groupByTagNameSumBI.forEach((tagName, sumBI) -> {
            tagRankingResDtos.add(new TagRankingResDto(tagName, sumBI));
        });
        List<TagRankingResDto> collect = tagRankingResDtos.stream()
                .sorted(Comparator.comparing(TagRankingResDto::getTagPower).reversed()).limit(limit).
                        collect(Collectors.toList());
        return collect;
    }

    public List<TagRankingResDto> getTagRankingFromTextOrderBySumBI(TagRankingFromTextReqDto tagRankingFromTextReqDto) throws ParseException {
        LatLng mapCenter = LatLng.newBuilder().setLongitude(tagRankingFromTextReqDto.getMapCenterLongitude())
                .setLatitude(tagRankingFromTextReqDto.getMapCenterLatitude()).build();
        List<FBalltag> byBallInTags = fBallTagQueryRepository.findByTextMatchTags(tagRankingFromTextReqDto.getSearchTagText(), mapCenter);
        return getListOrderByTagPower(mapCenter, 10, byBallInTags);
    }


    private List<FBalltag> calcTagRelationBallBI(LatLng position, List<FBalltag> byBallInTags) {
        return byBallInTags.stream().map(x -> {
            x.getBallUuid().setBI(ballOfInfluenceCalc.calc(x.getBallUuid(), position));
            return x;
        }).collect(Collectors.toList());
    }


    public List<TagRankingResDto> getRelationTagRankingFromTagNameOrderByBallPower(String searchTag) {
//        return fBallTagQueryRepository.getRelationTagRankingFromTagNameOrderByBallPower(searchTag);
        return null;
    }

    public List<FBallTagResDto> getTagFromBallUuid(String ballUuid) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        List<FBalltag> fBallTags = fBallTagDataRepository.findByBallUuid(fBall);
        List<FBallTagResDto> collect = fBallTags.stream().map(x -> new FBallTagResDto(x)).collect(Collectors.toList());
        return collect;
    }
}
