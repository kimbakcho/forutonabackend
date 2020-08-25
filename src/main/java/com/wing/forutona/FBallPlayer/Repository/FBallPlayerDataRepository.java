package com.wing.forutona.FBallPlayer.Repository;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBallPlayer.Domain.FBallPlayer;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FBallPlayerDataRepository extends JpaRepository<FBallPlayer,Long> {

    @EntityGraph(attributePaths = {"ballUuid"})
    Slice<FBallPlayer> findFBallPlayerByPlayerUid(String playerUid,Pageable pageable);

    @EntityGraph(attributePaths = {"ballUuid"})
    FBallPlayer findFBallPlayerByPlayerUidIsAndBallUuidIs(FUserInfo playerUid, FBall ballUuid);

}