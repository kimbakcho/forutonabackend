package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.Contributors;
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
public class BallDisLikeServiceImpl extends BallLikeService {

    final ContributorsDataRepository contributorsDataRepository;

    public BallDisLikeServiceImpl(FBallDataRepository fBallDataRepository,
                                  FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository,
                                  FBallValuationDataRepository fBallValuationDataRepository,
                                  ContributorsDataRepository contributorsDataRepository) {
        super(fBallDataRepository, fUserInfoSimpleDataRepository, fBallValuationDataRepository);
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    void setBallLikeData(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple) {
        fBall.plusBallDisLike(reqDto.getDisLikePoint());
    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfoSimple fUserInfoSimple,
                                     Optional<FBallValuation> fBallValuationOptional) {
        if (fBallValuationOptional.isPresent()) {
            FBallValuation fBallValuation = fBallValuationOptional.get();
            fBallValuation.setBallLike(0L);
            fBallValuation.setBallDislike(reqDto.getDisLikePoint());
            fBallValuation.setPoint(fBallValuation.getPoint() - reqDto.getDisLikePoint());
            return fBallValuation;
        } else {
            FBallValuation fBallValuation = FBallValuation.builder()
                    .valueUuid(reqDto.getValueUuid())
                    .ballUuid(fBall)
                    .point(0 - reqDto.getDisLikePoint())
                    .uid(fUserInfoSimple)
                    .ballLike(0L)
                    .ballDislike(reqDto.getDisLikePoint())
                    .build();
            FBallValuation saveFBallValuation = fBallValuationDataRepository.save(fBallValuation);
            return saveFBallValuation;
        }
    }

    @Override
    void setContributors(FBall fBall, FUserInfoSimple fUserInfoSimple, FBallValuation fBallValuation) {
        if (contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfoSimple, fBall).isEmpty()) {
            Contributors contributors = Contributors.builder().uid(fUserInfoSimple).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
    }


}
