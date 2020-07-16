package com.wing.forutona.FBall.Repository.FBall;

import com.google.firebase.auth.UserInfo;
import com.vividsolutions.jts.geom.Geometry;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Domain.FBalltag;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FBallDataRepository extends JpaRepository<FBall,String> {
    List<FBall> findByUid(FUserInfo uid);

}
