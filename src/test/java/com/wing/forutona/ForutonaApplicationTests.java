package com.wing.forutona;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.*;

import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import com.wing.forutona.FTag.Service.FTagService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@ActiveProfiles(profiles = "local")
@Transactional
class ForutonaApplicationTests {

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    FBallTagQueryRepository fBallTagQueryRepository;


    @Autowired
    DistanceOfBallCountToLimitService distanceOfBallCountToLimitService;

    @Autowired
    FTagService fTagService;

    @PersistenceContext
    EntityManager em;

    @Test
    public  void MysqlFullTextMatch(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        String text = "test";
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Integer.class,"function('match',{0},{1})", QFBall.fBall.ballName, "+ret*");
        Expression<Integer> test = ExpressionUtils.as(Expressions.constant(1), "test");
        List<FBall> fetch = queryFactory.select(QFBall.fBall).from(QFBall.fBall).where(booleanTemplate.eq(1)).fetch();
        for (FBall fBall : fetch) {
            System.out.println(fBall.getBallName());
        }
    }



    @Test
    public void getTagTagRanking(){
        TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto = new TagRankingFromBallInfluencePowerReqDto();
        tagRankingFromBallInfluencePowerReqDto.setLatitude(37.51368824280154);
        tagRankingFromBallInfluencePowerReqDto.setLongitude(126.8985465914011);
        tagRankingFromBallInfluencePowerReqDto.setLimit(10);
//        List<TagRankingDto> fTagRanking = fTagService.getFTagRanking(tagRankingReqDto);
//        for (TagRankingDto tagRankingDto : fTagRanking) {
//            System.out.println( "tag ranking = " + tagRankingDto.getRanking());
//            System.out.println( "tag name = " + tagRankingDto.getTagName());
//            System.out.println( "tag power = " + tagRankingDto.getTagPower());
//            System.out.println( "--------------------------------------------");
//        }
    }

    public Geometry createCircle(double x, double y, double radius) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new Coordinate(x, y));
        shapeFactory.setSize(radius * 2);
        return shapeFactory.createCircle();
    }

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

}
