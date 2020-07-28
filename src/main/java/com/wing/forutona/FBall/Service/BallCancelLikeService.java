package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface BallCancelLikeService {
    void cancelExecute(String ballUuid, String userUid) throws Exception;
}
@Service
@Transactional
@RequiredArgsConstructor
class  BallCancelLikeServiceImpl implements  BallCancelLikeService {

    final FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final ContributorsDataRepository contributorsDataRepository;

    @Override
    public void cancelExecute(String ballUuid, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw  new Exception("over Active Time can't not cancelLikeExecute");
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();
        FBallValuation fBallValuation = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfo).get();
        if(fBallValuation.getPoint() > 0){
            fBall.minusBallLike(fBallValuation.getPoint());
        }else {
            fBall.minusBallDisLike(fBallValuation.getPoint());
        }
        contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(fUserInfo,fBall);
        return;
    }
}
