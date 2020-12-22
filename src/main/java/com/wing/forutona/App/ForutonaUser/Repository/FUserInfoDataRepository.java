package com.wing.forutona.App.ForutonaUser.Repository;

import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FUserInfoDataRepository extends JpaRepository<FUserInfo,String> {
    long countByNickNameEquals(String NickName);

}
