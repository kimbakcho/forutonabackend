package com.wing.forutona.App.FBallValuation.Repositroy;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FBallValuationDataRepository extends JpaRepository<FBallValuation,String> {
    Optional<FBallValuation> findByBallUuidIsAndUidIs(FBall ballUuid, FUserInfo uid);
    Long countByBallUuidAndBallLikeNotOrBallDislikeNot(FBall ballUuid,Long likeCount,Long disLikeCount);
    void deleteByBallUuidAndUid(FBall ballUuid, FUserInfo uid);
}
