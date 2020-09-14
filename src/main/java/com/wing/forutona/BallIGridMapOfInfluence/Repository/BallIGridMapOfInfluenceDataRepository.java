package com.wing.forutona.BallIGridMapOfInfluence.Repository;

import com.wing.forutona.BallIGridMapOfInfluence.Domain.BallIGridMapOfInfluence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BallIGridMapOfInfluenceDataRepository extends JpaRepository<BallIGridMapOfInfluence, Long> {
    Optional<BallIGridMapOfInfluence> findByLatitudeAndLongitude(double latitude, double longitude);
}
