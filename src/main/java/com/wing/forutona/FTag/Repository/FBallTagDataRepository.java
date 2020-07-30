package com.wing.forutona.FTag.Repository;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Domain.FBalltag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FBallTagDataRepository  extends JpaRepository<FBalltag,Long> {
    List<FBalltag> findByBallUuid(FBall BallUuid);
}
