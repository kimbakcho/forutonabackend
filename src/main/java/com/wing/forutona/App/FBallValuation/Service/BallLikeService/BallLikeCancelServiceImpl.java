package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteReqDto;
import com.wing.forutona.App.Contributors.Repository.ContributorsDataRepository;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    void setBallLikeData(FBall fBall, FBallVoteReqDto reqDto, FUserInfo fUserInfo) {

    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallVoteReqDto reqDto, FUserInfo fUserInfoSimple) {
        return null;
    }

    @Override
    void setContributors(FBall fBall, FUserInfo fUserInfo, FBallValuation fBallValuation) {

    }

}
