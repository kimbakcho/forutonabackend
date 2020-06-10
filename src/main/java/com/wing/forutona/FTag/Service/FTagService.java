package com.wing.forutona.FTag.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Service.FBallService;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.*;
import com.wing.forutona.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FTagService {
    @Autowired
    FBallTagQueryRepository fBallTagQueryRepository;

    @Autowired
    FBallTagDataRepository fBallTagDataRepository;

    @Autowired
    FBallService fBallService;

    /*
    현재 사용자가 설정한 지도의 중심 위치에서 Top10 개의 영향력 Tag를 가지고 옴
    centerPoint가 사용자 설정의 지도 중심 위치
    Rect가 적정 검색 범위
     */
    @Async
    @Transactional
    public void getFTagRankingFromBallInfluencePower(ResponseBodyEmitter emitter, TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto) throws ParseException {
        int searchDistance = fBallService.distanceOfBallCountToLimit(tagRankingFromBallInfluencePowerReqDto.getLatitude(),
                tagRankingFromBallInfluencePowerReqDto.getLongitude(), 1000);
        Geometry rect = GisGeometryUtil.createRect(tagRankingFromBallInfluencePowerReqDto.getLatitude(), tagRankingFromBallInfluencePowerReqDto.getLongitude(), searchDistance);
        Geometry centerPoint = GisGeometryUtil.createCenterPoint(tagRankingFromBallInfluencePowerReqDto.getLatitude(), tagRankingFromBallInfluencePowerReqDto.getLongitude());
        try {
            emitter.send(fBallTagQueryRepository.getFindTagRankingInDistanceOfInfluencePower(centerPoint, rect, tagRankingFromBallInfluencePowerReqDto.getLimit()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }


    @Async
    @Transactional
    public void getRelationTagRankingFromTagNameOrderByBallPower(ResponseBodyEmitter emitter, RelationTagRankingFromTagNameReqDto reqDto) {
        try {
            TagRankingWrapdto tagSearchFromTextToTagRankings = fBallTagQueryRepository.getRelationTagRankingFromTagNameOrderByBallPower(reqDto);
            emitter.send(tagSearchFromTextToTagRankings);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }
    @Async
    @Transactional
    public void getTagFromBallUuid(ResponseBodyEmitter emitter, TagFromBallReqDto reqDto) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        List<FBalltag> fBalltagByBallUuidIs = fBallTagDataRepository.findFBalltagByBallUuidIs(fBall);
        List<TagResDto> collect = fBalltagByBallUuidIs.stream().map(x -> new TagResDto(x)).collect(Collectors.toList());
        TagResDtoWrap dtoWrap = new TagResDtoWrap();
        dtoWrap.setTotalCount(collect.size());
        dtoWrap.setTags(collect);
        try {
            emitter.send(dtoWrap);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
