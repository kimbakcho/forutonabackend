package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.Contributors;
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

import java.util.Optional;

import static java.lang.Math.abs;

@Service
@Transactional
public class BallLikeServiceImpl extends BallLikeService {


    final ContributorsDataRepository contributorsDataRepository;

    public BallLikeServiceImpl(FBallDataRepository fBallDataRepository,
                               FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository,
                               FBallValuationDataRepository fBallValuationDataRepository,
                               ContributorsDataRepository contributorsDataRepository
    ) {
        super(fBallDataRepository, fUserInfoSimpleDataRepository, fBallValuationDataRepository);
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple) {
        Optional<FBallValuation> fBallValuationOptional = fBallValuationDataRepository
                .findByBallUuidIsAndUidIs(fBall, fUserInfoSimple);
        if(fBallValuationOptional.isPresent()){
            FBallValuation fBallValuation = fBallValuationOptional.get();
            fBallValuation.setPoint(fBallValuation.getPoint() + reqDto.getLikePoint());
            return fBallValuation;
        }else {
            FBallValuation fBallValuation = FBallValuation.builder()
                    .valueUuid(reqDto.getValueUuid())
                    .ballUuid(fBall)
                    .point(0 + reqDto.getLikePoint())
                    .uid(fUserInfoSimple)
                    .build();
            fBallValuationDataRepository.save(fBallValuation);
            return fBallValuation;
        }
    }

    @Override
    void setContributors(FBall fBall, FUserInfoSimple fUserInfoSimple, FBallValuation fBallValuation) {
        if (contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfoSimple, fBall).isEmpty()) {
            Contributors contributors = Contributors.builder().uid(fUserInfoSimple).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
    }

    @Override
    void setBallLikeData(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple) {
        fBall.plusBallLike(reqDto.getLikePoint());
    }

}
