package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.Contributors;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class BallLikeService {

    final FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    abstract Integer setBallLikeData(FBall fBall,Integer point,FUserInfo userInfo);

    abstract void setFBallValuation(FBall fBall,FBallLikeReqDto reqDto,FUserInfo userInfo);

    abstract void setContributors(FBall fBall,FUserInfo userInfo);

    public Integer execute(FBallLikeReqDto reqDto, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw new Exception("over Active Time can't not likeExecute");
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();

        setFBallValuation(fBall,reqDto,fUserInfo);

        setContributors(fBall,fUserInfo);

        Integer result = setBallLikeData(fBall, reqDto.getPoint(),fUserInfo);
        return result;
    }
}

