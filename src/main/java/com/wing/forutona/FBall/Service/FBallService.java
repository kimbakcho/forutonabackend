package com.wing.forutona.FBall.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.FBall.Repository.MapFindScopeStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FBallService {

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    MapFindScopeStepRepository mapFindScopeStepRepository;


    @Async
    @Transactional
    public void getListUpBallFromMapArea(ResponseBodyEmitter emitter,BallFromMapAreaReqDto reqDto,
                                         MultiSorts sorts, Pageable pageable){
        try {
            emitter.send(fBallQueryRepository.getListUpBallFromMapArea(reqDto,sorts,pageable));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getListUpBallFromSearchText(ResponseBodyEmitter emitter, BallNameSearchReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        try {
            emitter.send(fBallQueryRepository.getListUpBallFromSearchText(reqDto,sorts,pageable));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getUserToMakerBalls(ResponseBodyEmitter emitter, UserToMakerBallReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        try {
            List<UserToMakerBallResDto> userToMakerBalls = fBallQueryRepository.getUserToMakerBalls(reqDto, sorts, pageable);
            UserToMakerBallResWrapDto resWrapDto = new UserToMakerBallResWrapDto(LocalDateTime.now(), userToMakerBalls);
            emitter.send(resWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void BallListUp(ResponseBodyEmitter emitter, FBallListUpReqDto reqDto, Pageable pageable) throws ParseException {
        int findDistanceRange = this.diatanceOfBallCountToLimit(reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getBallLimit());
        try {
            emitter.send(fBallQueryRepository.getBallListUp(
                    GisGeometryUtil.createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude())
                    , GisGeometryUtil.createRect(reqDto.getLatitude(), reqDto.getLongitude(), findDistanceRange)
                    , pageable));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }


    /*
    범위내 목적으로 하는 Ball갯수 까지 Rect(범위)를 확장 시켜 적합한 집계 거리 반환
     */
    public int diatanceOfBallCountToLimit(double latitude, double longitude, int limit) throws ParseException {
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
