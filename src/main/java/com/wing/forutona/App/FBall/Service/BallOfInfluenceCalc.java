package com.wing.forutona.App.FBall.Service;

import com.google.type.LatLng;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.BallIGridMapOfInfluence;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.LatitudeLongitude;
import com.wing.forutona.App.BallIGridMapOfInfluence.Repository.BallIGridMapOfInfluenceDataRepository;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.App.FBall.Domain.FBall;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public interface BallOfInfluenceCalc {
    Double calc(FBall fBall, LatLng position);

    List<FBall> calc(List<FBall> fBall, LatLng position);
}

@Transactional
@Service
@RequiredArgsConstructor
class BallOfInfluenceCalcImpl implements BallOfInfluenceCalc {
    final BallIGridMapOfInfluenceDataRepository ballIGridMapOfInfluenceDataRepository;

    double Coefficient1 = 1;
    double Coefficient2 = 1;

    @Override
    public Double calc(FBall fBall, LatLng position) {
        double latitude = Math.round(fBall.getLatitude() * 100.0) / 100.0;
        double longitude = Math.round(fBall.getLongitude() * 100.0) / 100.0;
        BallIGridMapOfInfluence ballIGridMapOfInfluence = ballIGridMapOfInfluenceDataRepository
                .findById(new LatitudeLongitude(latitude, longitude))
                .orElseGet(() -> getBallIGridMapOfInfluence(latitude, longitude));
        long leftTimeHours = fBall.getMakeTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
        double distance = GisGeometryUtil.getDistance(fBall.getPlacePoint(), position);

        double BI = (fBall.getBallPower() - (leftTimeHours * ballIGridMapOfInfluence.getHGABP() * Coefficient1)) /
                (Coefficient2 * distance);
        return BI;
    }

    @Override
    public List<FBall> calc(List<FBall> fBall, LatLng position) {
        return fBall.stream().map(x -> {
            x.setBI(calc(x, position));
            return x;
        }).collect(Collectors.toList());
    }

    public BallIGridMapOfInfluence getBallIGridMapOfInfluence(double latitude, double longitude) {
        BallIGridMapOfInfluence basicBallIGridMapOfInfluence = BallIGridMapOfInfluence
                .builder()
                .MGBC(1)
                .MGAU(1)
                .MGABP(1)
                .latitudeLongitude(new LatitudeLongitude(latitude, longitude))
                .HGABP(1)
                .SummaryUpdateTime(LocalDateTime.now())
                .build();
        return basicBallIGridMapOfInfluence;
    }
}
