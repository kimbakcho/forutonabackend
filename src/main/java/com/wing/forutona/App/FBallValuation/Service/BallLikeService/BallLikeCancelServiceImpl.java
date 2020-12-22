package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallLikeReqDto;
import com.wing.forutona.App.Contributors.Repository.ContributorsDataRepository;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
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
