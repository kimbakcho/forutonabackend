package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Dto.FBallLikeResDto;
import com.wing.forutona.FBall.Dto.FBallValuationResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoSimpleDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class BallLikeService {

    final FBallDataRepository fBallDataRepository;

    final FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;


    abstract void setBallLikeData(FBall fBall, FBallLikeReqDto point, FUserInfoSimple fUserInfoSimple);

    abstract FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple, Optional<FBallValuation> fBallValuationOptional);

    abstract void setContributors(FBall fBall, FUserInfoSimple fUserInfoSimple, FBallValuation fBallValuation);

    void setBallPower(FBall fBall) {
        fBall.setBallPower(fBall.getBallLikes() - fBall.getBallDisLikes());
    }

    public FBallLikeResDto execute(FBallLikeReqDto reqDto, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw new Exception("over Active Time can't not likeExecute");
        }
        FUserInfoSimple fUserInfoSimple = fUserInfoSimpleDataRepository.findById(userUid).get();

        Optional<FBallValuation> fBallValuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfoSimple);

        beforeValuationCancel(fBall,fBallValuationOptional);

        FBallValuation saveFBallValuation = setFBallValuation(fBall, reqDto, fUserInfoSimple, fBallValuationOptional);

        setBallLikeData(fBall, reqDto, fUserInfoSimple);

        setBallPower(fBall);

        setContributors(fBall, fUserInfoSimple, saveFBallValuation);

        Long likeServiceUseUserCount = fBallValuationDataRepository.countByBallUuidAndBallLikeNotOrBallDislikeNot(fBall, 0L, 0L);

        FBallLikeResDto fBallLikeResDto = new FBallLikeResDto();
        fBallLikeResDto.setBallLike(fBall.getBallLikes());
        fBallLikeResDto.setBallDislike(fBall.getBallDisLikes());
        fBallLikeResDto.setLikeServiceUseUserCount(likeServiceUseUserCount);
        fBallLikeResDto.setBallPower(fBall.getBallPower());
        fBallLikeResDto.setFballValuationResDto(new FBallValuationResDto(saveFBallValuation));

        return fBallLikeResDto;
    }

    public void beforeValuationCancel(FBall fBall,Optional<FBallValuation> fBallValuationOptional) {
        if (fBallValuationOptional.isPresent()) {
            FBallValuation fBallValuation = fBallValuationOptional.get();
            if(isBeforeLikeState(fBallValuation)){
                CancelLike(fBall, fBallValuation);
            }else if(isBeforeDisLikeState(fBallValuation)){
                CancelDisLike(fBall, fBallValuation);
            }
        }
    }

    public void CancelLike(FBall fBall, FBallValuation fBallValuation) {
        fBallValuation.setPoint(fBallValuation.getPoint() - fBallValuation.getBallLike());
        fBall.minusBallLike(fBallValuation.getBallLike());
        fBallValuation.setBallLike(0L);
    }

    public void CancelDisLike(FBall fBall, FBallValuation fBallValuation) {
        fBallValuation.setPoint(fBallValuation.getPoint() + fBallValuation.getBallDislike());
        fBall.minusBallDisLike(fBallValuation.getBallDislike());
        fBallValuation.setBallDislike(0L);
    }

    public boolean isBeforeLikeState(FBallValuation fBallValuation) {
        return fBallValuation.getBallLike() > 0;
    }
    public boolean isBeforeDisLikeState(FBallValuation fBallValuation) {
        return fBallValuation.getBallDislike() > 0;
    }

}

