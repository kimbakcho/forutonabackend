package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallLikeReqDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallLikeResDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallValuationResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
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

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;


    abstract void setBallLikeData(FBall fBall, FBallLikeReqDto point, FUserInfo fUserInfo);

    abstract FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfo, Optional<FBallValuation> fBallValuationOptional);

    abstract void setContributors(FBall fBall, FUserInfo fUserInfoSimple, FBallValuation fBallValuation);

    void setBallPower(FBall fBall) {
        fBall.setBallPower(fBall.getBallLikes() - fBall.getBallDisLikes());
    }

    public FBallLikeResDto execute(FBallLikeReqDto reqDto, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw new Exception("over Active Time can't not likeExecute");
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();

        Optional<FBallValuation> fBallValuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfo);

        beforeValuationCancel(fBall,fBallValuationOptional);

        FBallValuation saveFBallValuation = setFBallValuation(fBall, reqDto, fUserInfo, fBallValuationOptional);

        setBallLikeData(fBall, reqDto, fUserInfo);

        setBallPower(fBall);

        setContributors(fBall, fUserInfo, saveFBallValuation);

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

