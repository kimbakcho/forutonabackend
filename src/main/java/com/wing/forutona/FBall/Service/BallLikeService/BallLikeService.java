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

@Service
@RequiredArgsConstructor
@Transactional
public abstract class BallLikeService {

    final FBallDataRepository fBallDataRepository;

    final FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    abstract void setBallLikeData(FBall fBall, FBallLikeReqDto point,FUserInfoSimple fUserInfoSimple);

    abstract FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple);

    abstract void setContributors(FBall fBall, FUserInfoSimple fUserInfoSimple, FBallValuation fBallValuation);

    void setBallPower(FBall fBall){
        fBall.setBallPower(fBall.getBallLikes() - fBall.getBallDisLikes());
    };

    public FBallLikeResDto execute(FBallLikeReqDto reqDto, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw new Exception("over Active Time can't not likeExecute");
        }
        FUserInfoSimple fUserInfoSimple = fUserInfoSimpleDataRepository.findById(userUid).get();

        FBallValuation fBallValuation = setFBallValuation(fBall, reqDto, fUserInfoSimple);

        setBallLikeData(fBall, reqDto,fUserInfoSimple);

        setBallPower(fBall);

        setContributors(fBall,fUserInfoSimple,fBallValuation);

        FBallLikeResDto fBallLikeResDto = new FBallLikeResDto();
        fBallLikeResDto.setBallLike(fBall.getBallLikes());
        fBallLikeResDto.setBallDislike(fBall.getBallDisLikes());
        fBallLikeResDto.setFBallValuationResDto(new FBallValuationResDto(fBallValuation));

        return fBallLikeResDto;
    }
}

