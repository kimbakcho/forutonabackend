package com.wing.forutona.FBall.Repository.FBall;

import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Domain.FBalltag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FBallDataRepository extends JpaRepository<FBall,Long> {

    @Query("select count(fb.ballUuid) from FBall fb where within(fb.placePoint, :rect) = true ")
    Long getFindLocationWithin(@Param("rect") Geometry rect);
}
