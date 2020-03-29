package com.wing.forutona.ForutonaUser.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FUserInfoRepository extends JpaRepository<FUserInfo,Long> {


}
