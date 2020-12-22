package com.wing.forutona.App.BallIGridMapOfInfluence.Repository;

import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.BallIGridMapOfInfluence;
import com.wing.forutona.App.BallIGridMapOfInfluence.Domain.LatitudeLongitude;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BallIGridMapOfInfluenceDataRepository extends JpaRepository<BallIGridMapOfInfluence, LatitudeLongitude> {

}
