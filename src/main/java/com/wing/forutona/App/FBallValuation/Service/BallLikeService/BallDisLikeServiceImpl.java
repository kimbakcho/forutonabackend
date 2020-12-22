package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.Contributors.Domain.Contributors;
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
public class BallDisLikeServiceImpl extends BallLikeService {

    final ContributorsDataRepository contributorsDataRepository;

    public BallDisLikeServiceImpl(FBallDataRepository fBallDataRepository,
                                  FUserInfoDataRepository fUserInfoDataRepository,
                                  FBallValuationDataRepository fBallValuationDataRepository,
                                  ContributorsDataRepository contributorsDataRepository) {
        super(fBallDataRepository, fUserInfoDataRepository, fBallValuationDataRepository);
        this.contributorsDataRepository = contributorsDataRepository;
    }

    @Override
    void setBallLikeData(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfo) {
        fBall.plusBallDisLike(reqDto.getDisLikePoint());
    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallLikeReqDto reqDto, FUserInfo fUserInfo,
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
                    .uid(fUserInfo)
                    .ballLike(0L)
                    .ballDislike(reqDto.getDisLikePoint())
                    .build();
            FBallValuation saveFBallValuation = fBallValuationDataRepository.save(fBallValuation);
            return saveFBallValuation;
        }
    }

    @Override
    void setContributors(FBall fBall, FUserInfo fuserInfo, FBallValuation fBallValuation) {
        if (contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fuserInfo, fBall).isEmpty()) {
            Contributors contributors = Contributors.builder().uid(fuserInfo).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
    }


}
