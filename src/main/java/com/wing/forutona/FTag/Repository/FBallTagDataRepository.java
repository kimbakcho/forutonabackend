package com.wing.forutona.FTag.Repository;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Domain.FBalltag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBallTagDataRepository  extends JpaRepository<FBalltag,Long> {
    List<FBalltag> findFBalltagByBallUuidIs(FBall BallUuid);
}
