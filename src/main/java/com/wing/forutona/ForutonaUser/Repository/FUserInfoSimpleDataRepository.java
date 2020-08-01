package com.wing.forutona.ForutonaUser.Repository;

import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FUserInfoSimpleDataRepository extends JpaRepository<FUserInfoSimple,String> {
}
