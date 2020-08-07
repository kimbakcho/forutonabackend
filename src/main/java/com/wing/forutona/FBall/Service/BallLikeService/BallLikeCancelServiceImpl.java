package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoSimpleDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple,
                                     Optional<FBallValuation> fBallValuationOptional) {
        return fBallValuationOptional.get();
    }

    @Override
    void setContributors(FBall fBall, FUserInfoSimple fUserInfoSimple, FBallValuation fBallValuation) {
        if(fBallValuation.getPoint() == 0){
            contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(fUserInfoSimple,fBall);
        }
    }

}
