package com.wing.forutona.FBall.Service;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Dto.FBallListUpReqDto;
import com.wing.forutona.FBall.Dto.UserToMakerBallReqDto;
import com.wing.forutona.FBall.Dto.UserToMakerBallResWrapDto;
import com.wing.forutona.FBall.Dto.UserToMakerBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.MapFindScopeStep.Domain.FMapFindScopeStep;
import com.wing.forutona.MapFindScopeStep.Repository.MapFindScopeStepRepository;
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
    public void getUserToMakerBalls(ResponseBodyEmitter emitter, UserToMakerBallReqDto reqDto, MultiSorts sorts, Pageable pageable){
        try {
            List<UserToMakerBallResDto> userToMakerBalls = fBallQueryRepository.getUserToMakerBalls(reqDto,sorts ,pageable);
            UserToMakerBallResWrapDto resWrapDto = new UserToMakerBallResWrapDto(LocalDateTime.now(),userToMakerBalls);
            emitter.send(resWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void BallListUp(ResponseBodyEmitter emitter,FBallListUpReqDto reqDto, Pageable pageable) throws ParseException {
        int findDistanceRange = this.diatanceOfBallCountToLimit(reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getBallLimit());
        try {
            emitter.send(fBallQueryRepository.getBallListUp(
                    createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude())
                    , createRect(reqDto.getLatitude(), reqDto.getLongitude(), findDistanceRange)
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
            Geometry rect = createRect(latitude, longitude, currentScopeMater);
            if (fBallQueryRepository.getFindBallCountInDistance(rect) > limit) {
                return currentScopeMater;
            }
        }
        return currentScopeMater;
    }

    /*
      좌표를 받아 정사각형 범위를 구함
     */
    public Geometry createRect(double latitude, double longitude, double distance) throws ParseException {
        com.grum.geocalc.Coordinate lat = com.grum.geocalc.Coordinate.fromDegrees(latitude);
        com.grum.geocalc.Coordinate lng = com.grum.geocalc.Coordinate.fromDegrees(longitude);
        com.grum.geocalc.Point findPosition = com.grum.geocalc.Point.at(lat, lng);
        BoundingArea area = EarthCalc.around(findPosition, distance / 2);
        Geometry rect = new WKTReader().read(createRectPOLYGONStr(area.southWest.longitude, area.southWest.latitude, area.northEast.longitude, area.northEast.latitude));
        rect.setSRID(4326);
        return rect;
    }

    /*
    select ST_ASText(ST_Envelope(ST_GeomFromText("LineString(127.2854264529815 37.07021091269403, 127.675837138646 38.70836583519889)",4326)))
    위와 같은 함수를 대신함
    https://dev.mysql.com/doc/refman/8.0/en/gis-general-property-functions.html
     */
    public String createRectPOLYGONStr(double southWestlongitude,double southWestlatitude,double northEastlongitude,double northEastlatitude){
        String src = "POLYGON(("+southWestlongitude+" "+southWestlatitude+"," +
                northEastlongitude+" "+southWestlatitude+"," +
                northEastlongitude+" "+northEastlatitude+"," +
                southWestlongitude+" "+northEastlatitude+"," +
                southWestlongitude+" "+southWestlatitude+"))";
        return src;
    }

    public Geometry createCenterPoint(double latitude, double longitude) throws ParseException {
        Geometry circlePoint = new WKTReader().read("Point(" + latitude + " " + longitude + ")");
        circlePoint.setSRID(4326);
        return circlePoint;
    }

}
