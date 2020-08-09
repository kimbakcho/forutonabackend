package com.wing.forutona.FBall.Repository.FBall;

import com.wing.forutona.FBall.Domain.FBall;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FBallDataRepository extends JpaRepository<FBall,String> {
    List<FBall> findByUid(FUserInfo uid);

}
