package com.wing.forutona.FTag.Repository;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
class FBallTagQueryRepositoryTest extends BaseTest {

    @Autowired
    FBallTagDataRepository fBallTagDataRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FBallTagQueryRepository fBallTagQueryRepository;


    @Test
    @DisplayName("태그에 관련된 볼에 강한 영향력을 주입 후 해당 태그로 결과 받기")
    void getFindTagRankingInDistanceOfInfluencePower() throws ParseException {

        //given
        updateAllAliveBall();
        FBalltag fBalltag = randomChoiceTag();
        makeTagBallPowerStrong(fBalltag);
        //when
        TagRankingWrapdto influencePowerRankingDto = fBallTagQueryRepository.getFindTagRankingInDistanceOfInfluencePower(
                GisGeometryUtil.createCenterPoint(37.50298846403655, 126.89706021076441)
                , GisGeometryUtil.createRect(37.50298846403655, 126.89706021076441, 10000),
                1000);
        //then
        System.out.println(fBalltag.getTagItem());
        System.out.println(influencePowerRankingDto.getContents().get(0).getTagName());
        assertEquals(influencePowerRankingDto.getContents().get(0).getTagName(),fBalltag.getTagItem());

    }

    private List<FBall> updateAllAliveBall() {
        List<FBall> fBalls = fBallDataRepository.findAll();
        for (FBall ball: fBalls ) {
            ball.setActivationTime(LocalDateTime.now().plusDays(1));
        }
        return fBalls;
    }

    private FBalltag randomChoiceTag() {
        List<FBalltag> fBallTags = fBallTagDataRepository.findAll();
        double dValue = Math.random();
        int iValue = (int)(dValue * fBallTags.size());
        return fBallTags.get(iValue);
    }

    private void makeTagBallPowerStrong(FBalltag fBalltag) {
        FBall fBall = fBalltag.getBallUuid();
        fBall.setBallPower(10000);
        fBall.setPlacePoint(126.89716021076441,37.50298846403655);
    }
}