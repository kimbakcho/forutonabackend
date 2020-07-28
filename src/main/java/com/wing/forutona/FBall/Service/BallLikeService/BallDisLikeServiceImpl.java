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
import org.springframework.stereotype.Service;

@Service
public class BallDisLikeServiceImpl extends BallLikeService{
    final FBallValuationDataRepository fBallValuationDataRepository;

    final ContributorsDataRepository contributorsDataRepository;
    public BallDisLikeServiceImpl(FBallDataRepository fBallDataRepository, FUserInfoDataRepository fUserInfoDataRepository, FBallValuationDataRepository fBallValuationDataRepository, ContributorsDataRepository contributorsDataRepository) {
        super(fBallDataRepository, fUserInfoDataRepository);
        this.fBallValuationDataRepository = fBallValuationDataRepository;
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    Integer setBallLikeData(FBall fBall, Integer point,FUserInfo userInfo) {
        Integer totalBallLike = fBall.plusBallDisLike(point);
        return totalBallLike;
    }

    @Override
    void setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo userInfo) {
        FBallValuation fBallValuation = FBallValuation.builder()
                .valueUuid(reqDto.getBallUuid())
                .ballUuid(fBall)
                .point(changeMinusValue(reqDto.getPoint()))
                .uid(userInfo)
                .build();

        fBallValuationDataRepository.save(fBallValuation);
    }

    @Override
    void setContributors(FBall fBall, FUserInfo userInfo) {
        if(contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(userInfo,fBall).isEmpty()){
            Contributors contributors =  Contributors.builder().uid(userInfo).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
    }

    int changeMinusValue(Integer value) {
        return value * -1;
    }
}
