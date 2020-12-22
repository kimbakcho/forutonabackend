package com.wing.forutona.App.ForutonaUser.Repository;

import com.wing.forutona.App.ForutonaUser.Domain.UserPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPolicyDataRepository extends JpaRepository<UserPolicy,String> {
}
