package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Domain.Contributors;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallDisLikeReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface BallDisLikeService {
    Integer disLikeExecute(FBallDisLikeReqDto reqDto,String userUid) throws Exception;
}

@Service
@RequiredArgsConstructor
@Transactional
class BallDisLikeServiceImpl implements BallDisLikeService {

    final FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final ContributorsDataRepository contributorsDataRepository;

    @Override
    public Integer disLikeExecute(FBallDisLikeReqDto reqDto,String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw  new Exception("over Active Time can't not disLikeExecute");
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();

        FBallValuation fBallValuation = FBallValuation.builder()
                .valueUuid(reqDto.getBallUuid())
                .ballUuid(fBall)
                .point(changeMinusValue(reqDto.getPoint()))
                .uid(fUserInfo)
                .build();
        fBallValuationDataRepository.save(fBallValuation);
        if(contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo,fBall).isEmpty()){
            Contributors contributors =  Contributors.builder().uid(fUserInfo).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
        Integer totalBallLike = fBall.plusBallDisLike(reqDto.getPoint());
        return totalBallLike;
    }

    int changeMinusValue(Integer value) {
        return value * -1;
    }

}
