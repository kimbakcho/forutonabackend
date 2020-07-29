package com.wing.forutona.FBall.Service.BallLikeService;

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
public class BallLikeCancelServiceImpl extends BallLikeService {
    final FBallValuationDataRepository fBallValuationDataRepository;

    final ContributorsDataRepository contributorsDataRepository;

    public BallLikeCancelServiceImpl(FBallDataRepository fBallDataRepository,
                                     FUserInfoDataRepository fUserInfoDataRepository,
                                     FBallValuationDataRepository fBallValuationDataRepository,
                                     ContributorsDataRepository contributorsDataRepository) {
        super(fBallDataRepository, fUserInfoDataRepository);
        this.fBallValuationDataRepository = fBallValuationDataRepository;
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    Integer setBallLikeData(FBall fBall, Integer point,FUserInfo userInfo) {
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, userInfo).get();
        if(fBallValuation.getPoint() > 0){
            fBall.minusBallLike(fBallValuation.getPoint());
        }else {
            fBall.minusBallDisLike(fBallValuation.getPoint());
        }
        return 0;
    }

    @Override
    void setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo userInfo) {
        fBallValuationDataRepository.deleteByBallUuidAndUid(fBall, userInfo);
    }

    @Override
    void setContributors(FBall fBall, FUserInfo userInfo) {
        contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(userInfo,fBall);
    }
}
