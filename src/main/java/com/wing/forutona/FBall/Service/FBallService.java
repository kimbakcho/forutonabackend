package com.wing.forutona.FBall.Service;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import com.wing.forutona.FBall.Dto.FBallListUpReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.MapFindScopeStep.Domain.FMapFindScopeStep;
import com.wing.forutona.MapFindScopeStep.Repository.MapFindScopeStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FBallService {

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallQueryRepository fBallQueryRepository;


    @Autowired
    MapFindScopeStepRepository mapFindScopeStepRepository;


    public FBallListUpWrapDto BallListUp(FBallListUpReqDto reqDto, Pageable pageable) {
        int findDistanceRange = this.diatanceOfBallCountToLimit(reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getBallLimit());
        return fBallQueryRepository.getBallListUp(
                createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude())
                , createRect(reqDto.getLatitude(), reqDto.getLongitude(), findDistanceRange)
                , pageable);
    }


    /*
    범위내 목적으로 하는 Ball갯수 까지 Rect(범위)를 확장 시켜 적합한 집계 거리 반환
     */
    public int diatanceOfBallCountToLimit(double latitude, double longitude, int limit) {
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
    public Geometry createRect(double latitude, double longitude, double distance) {
        com.grum.geocalc.Coordinate lat = com.grum.geocalc.Coordinate.fromDegrees(latitude);
        com.grum.geocalc.Coordinate lng = com.grum.geocalc.Coordinate.fromDegrees(longitude);
        com.grum.geocalc.Point findPosition = com.grum.geocalc.Point.at(lat, lng);
        BoundingArea area = EarthCalc.around(findPosition, distance / 2);
        Coordinate southWest = new Coordinate(area.southWest.longitude, area.southWest.latitude);
        Coordinate northEast = new Coordinate(area.northEast.longitude, area.northEast.latitude);
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        Envelope envelope = new Envelope(southWest, northEast);
        shapeFactory.setEnvelope(envelope);
        Geometry rectangle = shapeFactory.createRectangle();
        rectangle.setSRID(4326);
        return rectangle;
    }

    public Geometry createCenterPoint(double latitude, double longitude) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        Coordinate centerPoint1 = new Coordinate(latitude, longitude);
        shapeFactory.setCentre(centerPoint1);
        shapeFactory.setSize(0.00000001);
        Geometry mapCenterCircle = shapeFactory.createCircle();
        mapCenterCircle.setSRID(4326);
        return mapCenterCircle;
    }

}
