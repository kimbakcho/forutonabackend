package com.wing.forutona.FBall.Service.BallListup;

import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.FBall.Dto.FBallListUpFromSearchTitleReqDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BallListUpFromSearchTitle implements BallListUpService {

    final FBallQueryRepository fBallQueryRepository;

    @Override
    public Page<FBallResDto> search(Object reqDto, FSorts sort, Pageable pageable) throws Exception {
        if (!(reqDto instanceof FBallListUpFromSearchTitleReqDto)) {
            throw new Exception("Not Type FBallListUpFromSearchTitleReqDto");
        }

        return fBallQueryRepository.getBallListUpFromSearchTitle((FBallListUpFromSearchTitleReqDto) reqDto, sort, pageable);
    }
}
