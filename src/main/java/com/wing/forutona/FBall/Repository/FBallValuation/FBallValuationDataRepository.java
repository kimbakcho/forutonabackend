package com.wing.forutona.FBall.Repository.FBallValuation;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallValuationResDto;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBallValuationDataRepository extends JpaRepository<FBallValuation,Long> {
    List<FBallValuation> findByBallUuidIsAndUidIs(FBall ballUuid, FUserInfo uid);
}
