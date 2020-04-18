package com.wing.forutona.ForutonaUser.Repository;

import com.wing.forutona.ForutonaUser.Domain.UserPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPolicyDataRepository extends JpaRepository<UserPolicy,String> {
}
