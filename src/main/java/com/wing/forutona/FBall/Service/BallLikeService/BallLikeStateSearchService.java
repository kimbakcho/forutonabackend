package com.wing.forutona.FBall.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallLikeResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.FBall.Service.BallValuation.BallUserValuationSearch;
import com.wing.forutona.FBall.Service.BallValuation.BallUserValuationSearchFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BallLikeStateSearchService {
    FBallLikeResDto getLikeState(String ballUuid, String uid) throws Exception;
}

@Service
@Transactional
@RequiredArgsConstructor
class BallLikeStateSearchServiceImpl implements BallLikeStateSearchService {

    final FBallDataRepository fBallDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;


    final BallUserValuationSearchFactory ballUserValuationSearchFactory;

    @Override
    public FBallLikeResDto getLikeState(String ballUuid, String uid) throws Exception {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();

        Long likeServiceUseUserCount = fBallValuationDataRepository
                .countByBallUuidAndBallLikeNotOrBallDislikeNot(fBall, 0L, 0L);

        FBallLikeResDto fBallLikeResDto = new FBallLikeResDto();

        fBallLikeResDto.setBallLike(fBall.getBallLikes());
        fBallLikeResDto.setBallDislike(fBall.getBallDisLikes());
        fBallLikeResDto.setLikeServiceUseUserCount(likeServiceUseUserCount);
        fBallLikeResDto.setBallPower(fBall.getBallPower());
        BallUserValuationSearch searchEngine = ballUserValuationSearchFactory.getSearchEngine(uid);
        fBallLikeResDto.setFballValuationResDto(searchEngine.search(ballUuid,uid));
        return fBallLikeResDto;
    }



}