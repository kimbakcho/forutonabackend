package com.wing.forutona.FTag.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Service.FBallService;
import com.wing.forutona.FTag.Dto.TagRankingReqDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import com.wing.forutona.FTag.Dto.TagSearchFromTextReqDto;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

@Service
public class FTagService {
    @Autowired
    FBallTagQueryRepository fBallTagQueryRepository;

    @Autowired
    FBallService fBallService;

    /*
    현재 사용자가 설정한 지도의 중심 위치에서 Top10 개의 영향력 Tag를 가지고 옴
    centerPoint가 사용자 설정의 지도 중심 위치
    Rect가 적정 검색 범위
     */
    @Async
    @Transactional
    public void getFTagRanking(ResponseBodyEmitter emitter, TagRankingReqDto tagRankingReqDto) throws ParseException {
        int searchDistance = fBallService.diatanceOfBallCountToLimit(tagRankingReqDto.getLatitude(),
                tagRankingReqDto.getLongitude(), 1000);
        Geometry rect = GisGeometryUtil.createRect(tagRankingReqDto.getLatitude(), tagRankingReqDto.getLongitude(), searchDistance);
        Geometry centerPoint = GisGeometryUtil.createCenterPoint(tagRankingReqDto.getLatitude(), tagRankingReqDto.getLongitude());
        try {
            emitter.send(fBallTagQueryRepository.getFindTagRankingInDistanceOfInfluencePower(centerPoint, rect, tagRankingReqDto.getLimit()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getTagSearchFromTextToBalls(ResponseBodyEmitter emitter, TagSearchFromTextReqDto reqDto,
                                            MultiSorts sorts, Pageable pageable) {
        try {
            FBallListUpWrapDto tagSearchFromTextToBalls = fBallTagQueryRepository.getTagSearchFromTextToBalls(reqDto, sorts, pageable);
            emitter.send(tagSearchFromTextToBalls);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getTagSearchFromTextToTagRankings(ResponseBodyEmitter emitter, TagSearchFromTextReqDto reqDto) {
        try {
            TagRankingWrapdto tagSearchFromTextToTagRankings = fBallTagQueryRepository.getTagSearchFromTextToTagRankings(reqDto);
            emitter.send(tagSearchFromTextToTagRankings);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }
}
