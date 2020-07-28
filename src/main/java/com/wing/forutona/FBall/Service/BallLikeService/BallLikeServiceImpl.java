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
public class BallLikeServiceImpl extends BallLikeService {
    final FBallValuationDataRepository fBallValuationDataRepository;

    final ContributorsDataRepository contributorsDataRepository;

    public BallLikeServiceImpl(FBallDataRepository fBallDataRepository,
                               FUserInfoDataRepository fUserInfoDataRepository,
                               FBallValuationDataRepository fBallValuationDataRepository,
                               ContributorsDataRepository contributorsDataRepository
    ) {
        super(fBallDataRepository, fUserInfoDataRepository);
        this.fBallValuationDataRepository = fBallValuationDataRepository;
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    void setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo userInfo) {
        FBallValuation fBallValuation = FBallValuation.builder()
                .valueUuid(reqDto.getValueUuid())
                .ballUuid(fBall)
                .point(reqDto.getPoint())
                .uid(userInfo)
                .build();

        this.fBallValuationDataRepository.save(fBallValuation);
    }

    @Override
    void setContributors(FBall fBall,FUserInfo userInfo) {
        if (contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(userInfo, fBall).isEmpty()) {
            Contributors contributors = Contributors.builder().uid(userInfo).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
    }

    @Override
    Integer setBallLikeData(FBall fBall,Integer point,FUserInfo userInfo) {
        Integer totalBallLike = fBall.plusBallLike(point);
        return totalBallLike;
    }

}
