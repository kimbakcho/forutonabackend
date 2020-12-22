package com.wing.forutona.App.FBall.Repository;

import com.wing.forutona.App.FBall.Domain.FBall;

import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FBallDataRepository extends JpaRepository<FBall,String> {
    List<FBall> findByUid(FUserInfo uid);

}
