package com.wing.forutona.FBall.Repository.FBallValuation;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FBallValuationDataRepository extends JpaRepository<FBallValuation,String> {
    Optional<FBallValuation> findByBallUuidIsAndUidIs(FBall ballUuid, FUserInfoSimple uid);
    void deleteByBallUuidAndUid(FBall ballUuid, FUserInfo uid);
}
