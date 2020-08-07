package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallLikeResDto;
import com.wing.forutona.FBall.Dto.FBallValuationResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoSimpleDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BallLikeStateSearchService {
    FBallLikeResDto getLikeState(String ballUuid, String uid);
}

@Service
@Transactional
@RequiredArgsConstructor
class BallLikeStateSearchServiceImpl implements BallLikeStateSearchService {

    final FBallDataRepository fBallDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository;

    @Override
    public FBallLikeResDto getLikeState(String ballUuid, String uid) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();

        Long likeServiceUseUserCount = fBallValuationDataRepository
                .countByBallUuidAndBallLikeNotOrBallDislikeNot(fBall, 0L, 0L);

        FBallLikeResDto fBallLikeResDto = new FBallLikeResDto();
        fBallLikeResDto.setBallLike(fBall.getBallLikes());
        fBallLikeResDto.setBallDislike(fBall.getBallDisLikes());
        fBallLikeResDto.setLikeServiceUseUserCount(likeServiceUseUserCount);
        fBallLikeResDto.setBallPower(fBall.getBallPower());
        Optional<FUserInfoSimple> userInfoSimpleOptional = fUserInfoSimpleDataRepository.findById(uid);
        if (userInfoSimpleOptional.isPresent()) {
            Optional<FBallValuation> fBallValuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, userInfoSimpleOptional.get());
            if (fBallValuationOptional.isPresent()) {
                fBallLikeResDto.setFballValuationResDto(new FBallValuationResDto(fBallValuationOptional.get()));
            }
        }
        return fBallLikeResDto;
    }

}