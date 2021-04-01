package com.wing.forutona.App.FBall.Service;

import com.google.type.LatLng;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.BallIGridMapOfInfluence;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.LatitudeLongitude;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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


    @Override
    public Double calc(FBall fBall, LatLng position) {

        double distance = GisGeometryUtil.getDistance(fBall.getPlacePoint(), position);
        double BI = (fBall.getBallPower() + 1 / distance);
        return BI;
    }

    @Override
    public List<FBall> calc(List<FBall> fBall, LatLng position) {
        return fBall.stream().map(x -> {
            x.setBI(calc(x, position));
            return x;
        }).collect(Collectors.toList());
    }

}
