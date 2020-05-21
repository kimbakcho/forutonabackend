package com.wing.forutona.FBall.Repository.FBallPlayer;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface FBallPlayerDataRepositroy extends JpaRepository<FBallPlayer,Long> {

    @EntityGraph(attributePaths = {"ballUuid"})
    Slice<FBallPlayer> findFBallPlayerByPlayerUid(String playerUid,Pageable pageable);

    @EntityGraph(attributePaths = {"ballUuid"})
    FBallPlayer findFBallPlayerByPlayerUidIsAndBallUuidIs(FUserInfo playerUid, FBall ballUuid);

}
