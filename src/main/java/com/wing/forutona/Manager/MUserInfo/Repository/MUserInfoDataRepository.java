package com.wing.forutona.Manager.MUserInfo.Repository;

import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MUserInfoDataRepository extends JpaRepository<MUserInfo,String> {
}
