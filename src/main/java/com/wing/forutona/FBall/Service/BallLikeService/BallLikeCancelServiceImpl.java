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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BallLikeCancelServiceImpl extends BallLikeService {

    final ContributorsDataRepository contributorsDataRepository;

    public BallLikeCancelServiceImpl(FBallDataRepository fBallDataRepository,
                                     FUserInfoDataRepository fUserInfoDataRepository,
                                     FBallValuationDataRepository fBallValuationDataRepository,
                                     ContributorsDataRepository contributorsDataRepository) {
        super(fBallDataRepository, fUserInfoDataRepository, fBallValuationDataRepository);
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    void setBallLikeData(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfo) {

    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfoSimple,
                                     Optional<FBallValuation> fBallValuationOptional) {
        return fBallValuationOptional.get();
    }

    @Override
    void setContributors(FBall fBall, FUserInfo fUserInfo, FBallValuation fBallValuation) {
        if(fBallValuation.getPoint() == 0){
            contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(fUserInfo,fBall);
        }
    }

}
