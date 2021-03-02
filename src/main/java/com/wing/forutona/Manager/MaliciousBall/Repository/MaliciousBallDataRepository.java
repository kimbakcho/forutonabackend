package com.wing.forutona.Manager.MaliciousBall.Repository;

import com.wing.forutona.Manager.MaliciousBall.Domain.MaliciousBall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaliciousBallDataRepository extends JpaRepository<MaliciousBall,Integer> {

    Optional<MaliciousBall> findByBallUuid(String ballUuid);

}
