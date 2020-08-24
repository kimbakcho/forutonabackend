package com.wing.forutona.Contributors.Repository;

import com.wing.forutona.Contributors.Domain.Contributors;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContributorsDataRepository extends JpaRepository<Contributors,Long> {
    Optional<Contributors> findContributorsByUidIsAndBallUuidIs(FUserInfo uid, FBall ballUuid);
    int deleteContributorsByUidIsAndBallUuidIs(FUserInfo uid, FBall ballUuid);
}
