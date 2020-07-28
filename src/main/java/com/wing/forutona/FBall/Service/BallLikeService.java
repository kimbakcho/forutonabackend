package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Domain.Contributors;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallLikeReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface BallLikeService {
     Integer likeExecute(FBallLikeReqDto reqDto,String userUid) throws Exception;
}

@Service
@RequiredArgsConstructor
@Transactional
class BallLikeServiceImpl implements  BallLikeService {

    final  FBallDataRepository fBallDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final ContributorsDataRepository contributorsDataRepository;

    @Override
    public Integer likeExecute(FBallLikeReqDto reqDto, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
            throw  new Exception("over Active Time can't not likeExecute");
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();

        FBallValuation fBallValuation = FBallValuation.builder()
                .valueUuid(reqDto.getBallUuid())
                .ballUuid(fBall)
                .point(reqDto.getPoint())
                .uid(fUserInfo)
                .build();
        fBallValuationDataRepository.save(fBallValuation);
        if(contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo,fBall).isEmpty()){
            Contributors contributors =  Contributors.builder().uid(fUserInfo).ballUuid(fBall).build();
            contributorsDataRepository.save(contributors);
        }
        Integer totalBallLike = fBall.plusBallLike(reqDto.getPoint());

        return totalBallLike;
    }
}
