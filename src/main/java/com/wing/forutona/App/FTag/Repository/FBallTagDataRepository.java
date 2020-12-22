package com.wing.forutona.App.FTag.Repository;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FTag.Domain.FBalltag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBallTagDataRepository  extends JpaRepository<FBalltag,Long> {
    List<FBalltag> findByBallUuid(FBall BallUuid);
}
