package com.wing.forutona.FBallValuation.Service.BallLikeService;

import com.wing.forutona.Contributors.Domain.Contributors;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.FBallValuation.Dto.FBallLikeReqDto;
import com.wing.forutona.Contributors.Repository.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.Math.abs;

@Service
@Transactional
public class BallLikeServiceImpl extends BallLikeService {


    final ContributorsDataRepository contributorsDataRepository;

    public BallLikeServiceImpl(FBallDataRepository fBallDataRepository,
                               FUserInfoDataRepository fUserInfoDataRepository,
                               FBallValuationDataRepository fBallValuationDataRepository,
                               ContributorsDataRepository contributorsDataRepository
    ) {
        super(fBallDataRepository, fUserInfoDataRepository, fBallValuationDataRepository);
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfo,
                                     Optional<FBallValuation> fBallValuationOptional) {
        if(fBallValuationOptional.isPresent()){

            FBallValuation fBallValuation = fBallValuationOptional.get();
            fBallValuation.setBallLike(reqDto.getLikePoint());
            fBallValuation.setBallDislike(0L);
            fBallValuation.setPoint(fBallValuation.getPoint() + reqDto.getLikePoint());
            return fBallValuation;
        }else {
            FBallValuation fBallValuation = FBallValuation.builder()
                    .valueUuid(reqDto.getValueUuid())
                    .ballUuid(fBall)
                    .point(0 + reqDto.getLikePoint())
                    .uid(fUserInfo)
                    .ballLike(reqDto.getLikePoint())
                    .ballDislike(0L)
                    .build();
            FBallValuation saveItem = fBallValuationDataRepository.save(fBallValuation);
            return saveItem;
        }
    }

    @Override
    void setContributors(FBall fBall, FUserInfo fUserInfo, FBallValuation fBallValuation) {
        if (contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo, fBall).isEmpty()) {
            Contributors contributors = Contributors.builder().uid(fUserInfo).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
    }

    @Override
    void setBallLikeData(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfo) {
        fBall.plusBallLike(reqDto.getLikePoint());
    }

}
