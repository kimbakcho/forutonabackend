package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoSimpleDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BallLikeCancelServiceImpl extends BallLikeService {

    final ContributorsDataRepository contributorsDataRepository;

    public BallLikeCancelServiceImpl(FBallDataRepository fBallDataRepository,
                                     FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository,
                                     FBallValuationDataRepository fBallValuationDataRepository,
                                     ContributorsDataRepository contributorsDataRepository) {
        super(fBallDataRepository, fUserInfoSimpleDataRepository, fBallValuationDataRepository);
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    void setBallLikeData(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple) {
        fBall.minusBallLike(reqDto.getLikePoint());
        fBall.minusBallDisLike(reqDto.getDisLikePoint());
    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple) {
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfoSimple).get();
        fBallValuation.setPoint(fBallValuation.getPoint() - reqDto.getLikePoint());
        fBallValuation.setPoint(fBallValuation.getPoint() + reqDto.getDisLikePoint());
        fBallValuation.setBallLike(fBallValuation.getBallLike() - reqDto.getLikePoint());
        fBallValuation.setBallLike(fBallValuation.getBallDislike() - reqDto.getDisLikePoint());
        return fBallValuation;
    }

    @Override
    void setContributors(FBall fBall, FUserInfoSimple fUserInfoSimple, FBallValuation fBallValuation) {
        if(fBallValuation.getPoint() == 0){
            contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(fUserInfoSimple,fBall);
        }
    }

}
