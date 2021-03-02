package com.wing.forutona.App.FBallValuation.Service.BallLikeService;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBallValuation.Dto.FBallValuationResDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallVoteResDto;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.FBallValuation.Service.BallUserValuationSearch;
import com.wing.forutona.App.FBallValuation.Service.BallUserValuationSearchFactory;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface BallVoteStateSearchService {
    FBallVoteResDto getVoteState(String ballUuid, String uid) throws Exception;
}

@Service
@Transactional
@RequiredArgsConstructor
class BallVoteStateSearchServiceImpl implements BallVoteStateSearchService {

    final FBallDataRepository fBallDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final BallUserValuationSearchFactory ballUserValuationSearchFactory;

    final FUserInfoDataRepository fUserInfoDataRepository;

    @Override
    public FBallVoteResDto getVoteState(String ballUuid, String uid) throws Exception {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();

        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();

        List<FBallValuation> byBallUuidIsAndUid = fBallValuationDataRepository.findByBallUuidIsAndUid(fBall, fUserInfo);

        Integer totalBallLikeFromUser = 0;
        Integer totalBallDisLikeFromUser = 0;

        for (FBallValuation valuationItem : byBallUuidIsAndUid) {
            totalBallLikeFromUser += valuationItem.getBallLike();
            totalBallDisLikeFromUser += valuationItem.getBallDislike();
        }

        FBallVoteResDto fBallVoteResDto = new FBallVoteResDto();

        fBallVoteResDto.setBallLike(totalBallLikeFromUser);
        fBallVoteResDto.setBallDislike(totalBallDisLikeFromUser);

        fBallVoteResDto.setBallPower(fBall.getBallPower());
        List<FBallValuation> votes = fBallValuationDataRepository.findByBallUuidIsAndUid(fBall, fUserInfo);
        List<FBallValuationResDto> voteResDtos = votes.stream().map(x -> new FBallValuationResDto(x)).collect(Collectors.toList());
        fBallVoteResDto.setFballValuationResDtos(voteResDtos);
        return fBallVoteResDto;
    }


}