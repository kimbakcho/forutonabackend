package com.wing.forutona.FBall.Service;

import com.google.type.LatLng;
import com.wing.forutona.BallIGridMapOfInfluence.Domain.BallIGridMapOfInfluence;
import com.wing.forutona.BallIGridMapOfInfluence.Repository.BallIGridMapOfInfluenceDataRepository;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.Optional;

public interface BallOfInfluenceCalc {
    double calc(FBall fBall, LatLng userPosition);
}

@Transactional
class BallOfInfluenceCalcImpl implements BallOfInfluenceCalc{
    BallIGridMapOfInfluenceDataRepository ballIGridMapOfInfluenceDataRepository;

    double Coefficient1 = 1;
    double Coefficient2 = 1;

    BallOfInfluenceCalcImpl(BallIGridMapOfInfluenceDataRepository ballIGridMapOfInfluenceDataRepository){
        this.ballIGridMapOfInfluenceDataRepository = ballIGridMapOfInfluenceDataRepository;
    }

    @Override
    public double calc(FBall fBall, LatLng userPosition) {
        double latitude = Math.round(fBall.getLatitude()*100.0)/100.0;
        double longitude = Math.round(fBall.getLongitude()*100.0)/100.0;
        BallIGridMapOfInfluence ballIGridMapOfInfluence = ballIGridMapOfInfluenceDataRepository
                .findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> getBallIGridMapOfInfluence(latitude, longitude));
        long leftTimeHours = fBall.getMakeTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
        double distance = GisGeometryUtil.getDistance(fBall.getPlacePoint(),userPosition) ;

        double BI = (fBall.getBallPower()-(leftTimeHours*ballIGridMapOfInfluence.getHGABP()*Coefficient1))/
                (Coefficient2*distance);
        return BI;
    }

    public BallIGridMapOfInfluence getBallIGridMapOfInfluence(double latitude, double longitude) {
        BallIGridMapOfInfluence basicBallIGridMapOfInfluence = BallIGridMapOfInfluence
                .builder()
                .MGBC(1)
                .MGAU(1)
                .MGABP(1)
                .longitude(longitude)
                .latitude(latitude)
                .HGABP(1)
                .SummaryUpdateTime(LocalDateTime.now())
                .build();
        return basicBallIGridMapOfInfluence;
    }
}
