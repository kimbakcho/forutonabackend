package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteReqDto;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.lang.Math.abs;

@Service
@Transactional
public class BallVoteServiceImpl extends BallLikeService {

    public BallVoteServiceImpl(FBallDataRepository fBallDataRepository, FUserInfoDataRepository fUserInfoDataRepository, FBallValuationDataRepository fBallValuationDataRepository) {
        super(fBallDataRepository, fUserInfoDataRepository, fBallValuationDataRepository);
    }

    @Override
    void setBallLikeData(FBall fBall, FBallVoteReqDto point, FUserInfo fUserInfo) {
        fBall.plusBallLike(point.getLikePoint());
        fBall.plusBallDisLike(point.getDisLikePoint());
    }

    @Override
    FBallValuation setFBallValuation(FBall fBall, FBallVoteReqDto reqDto, FUserInfo fUserInfo) {

        FBallValuation fBallValuation = FBallValuation.builder()
                .ballDislike(reqDto.getDisLikePoint())
                .ballLike(reqDto.getLikePoint())
                .point(reqDto.getLikePoint() - reqDto.getDisLikePoint())
                .ballUuid(fBall)
                .uid(fUserInfo)
                .valueUuid(UUID.randomUUID().toString())
                .build();
        int useTicket = abs(reqDto.getDisLikePoint()) + abs(reqDto.getLikePoint());

        fUserInfo.setInfluenceTicket(fUserInfo.getInfluenceTicket() - useTicket);

        FBallValuation saveItem = fBallValuationDataRepository.save(fBallValuation);

        return saveItem;
    }

    @Override
    void setContributors(FBall fBall, FUserInfo fUserInfoSimple, FBallValuation fBallValuation) {

    }
}
