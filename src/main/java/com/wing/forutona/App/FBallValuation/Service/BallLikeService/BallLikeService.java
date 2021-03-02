package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteReqDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteResDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallValuationResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class BallLikeService {

    final FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;


    abstract void setBallLikeData(FBall fBall, FBallVoteReqDto point, FUserInfo fUserInfo);

    abstract FBallValuation setFBallValuation(FBall fBall, FBallVoteReqDto reqDto, FUserInfo fUserInfo);

    abstract void setContributors(FBall fBall, FUserInfo fUserInfoSimple, FBallValuation fBallValuation);

    void setBallPower(FBall fBall) {
        fBall.setBallPower(fBall.getBallLikes() - fBall.getBallDisLikes());
    }

    public FBallVoteResDto execute(FBallVoteReqDto reqDto, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw new Exception("over Active Time can't not likeExecute");
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();


        FBallValuation saveFBallValuation = setFBallValuation(fBall, reqDto, fUserInfo);

        setBallLikeData(fBall, reqDto, fUserInfo);

        setBallPower(fBall);

        setContributors(fBall, fUserInfo, saveFBallValuation);

        FBallVoteResDto fBallVoteResDto = new FBallVoteResDto();
        fBallVoteResDto.setBallLike(fBall.getBallLikes());
        fBallVoteResDto.setBallDislike(fBall.getBallDisLikes());
        fBallVoteResDto.setBallPower(fBall.getBallPower());

        List<FBallValuation> byBallUuidIsAndUid = fBallValuationDataRepository.findByBallUuidIsAndUid(fBall, fUserInfo);

        List<FBallValuationResDto>valuationResDtos = byBallUuidIsAndUid.stream().map(x -> new FBallValuationResDto(x)).collect(Collectors.toList());

        fBallVoteResDto.setFballValuationResDtos(valuationResDtos);

        return fBallVoteResDto;
    }


}

