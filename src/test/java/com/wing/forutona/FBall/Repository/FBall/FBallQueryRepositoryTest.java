package com.wing.forutona.FBall.Repository.FBall;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@Transactional
class FBallQueryRepositoryTest extends BaseTest {

    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Test
    @DisplayName("최고 강한 영향력 볼 만들어 리턴 받기")
    void getBallListUpFromBallInfluencePower() throws ParseException {
        //given
        List<FBall> fBalls = updateAllAliveBall();
        FBall choiceFBall = randomChoiceStringInfluencePowerBall(fBalls);
        makeStrongInfluencePowerBall(choiceFBall);

        Pageable pageable = PageRequest.of(0, 9999999);
        //when
        FBallListUpWrapDto ballListUpFromBallInfluencePower = fBallQueryRepository.getBallListUpFromBallInfluencePower(
                GisGeometryUtil.createCenterPoint(37.50198846403655, 126.89106021076441),
                GisGeometryUtil.createRect(37.50198846403655, 126.89106021076441, 100000.0),
                pageable
        );

        then(ballListUpFromBallInfluencePower.getBalls().size()).isGreaterThan(0);
        then(ballListUpFromBallInfluencePower.getBalls().get(0).getBallUuid()).isEqualTo(choiceFBall.getBallUuid());
    }

    private FBall randomChoiceStringInfluencePowerBall(List<FBall> fBalls) {
        double dValue = Math.random();
        int iValue = (int)(dValue * fBalls.size());
        return fBalls.get(iValue);
    }

    private List<FBall> updateAllAliveBall() {
        List<FBall> fBalls = fBallDataRepository.findAll();
        for (FBall ball: fBalls ) {
            ball.setActivationTime(LocalDateTime.now().plusDays(1));
        }
        return fBalls;
    }

    private void makeStrongInfluencePowerBall(FBall fBall) {
        fBall.setBallPower(100);
        fBall.setLongitude(126.89706021076441);
        fBall.setLatitude(37.50298846403655);
        GeometryFactory geomFactory = new GeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(126.89706021076441,  37.50298846403655));
        point.setSRID(4326);
        fBall.setPlacePoint(point);
    }

}