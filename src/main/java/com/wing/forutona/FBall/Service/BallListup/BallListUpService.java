package com.wing.forutona.FBall.Service.BallListup;

import com.wing.forutona.CustomUtil.FSort;
import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;


public interface BallListUpService {

    Page<FBallResDto> search(Object reqDto, FSorts sort, Pageable pageable) throws Exception;
}
