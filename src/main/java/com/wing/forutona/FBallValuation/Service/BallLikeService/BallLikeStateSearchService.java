package com.wing.forutona.FBallValuation.Service.BallLikeService;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBallValuation.Dto.FBallLikeResDto;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.FBallValuation.Service.BallUserValuationSearch;
import com.wing.forutona.FBallValuation.Service.BallUserValuationSearchFactory;
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