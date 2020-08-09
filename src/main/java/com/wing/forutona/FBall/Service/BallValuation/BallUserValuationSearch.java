package com.wing.forutona.FBall.Service.BallValuation;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Dto.FBallValuationResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BallUserValuationSearch {
    FBallValuationResDto search(String ballUuid, String userUid) throws Exception;
}

@Service
@Transactional
@RequiredArgsConstructor
class BallSignUserValuationSearchImpl implements BallUserValuationSearch {

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final FBallDataRepository fBallDataRepository;

    @Override
    public FBallValuationResDto search(String ballUuid, String userUid) throws Exception {

        FUserInfo userInfo = fUserInfoDataRepository.findById(userUid).get();
        FBall fBall = fBallDataRepository.findById(ballUuid).get();

        Optional<FBallValuation> fBallValuationOptional = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, userInfo);
        if (fBallValuationOptional.isPresent()) {
            return new FBallValuationResDto(fBallValuationOptional.get());
        } else {
            FBallValuationResDto fBallValuationResDto = new FBallValuationResDto();
            fBallValuationResDto.setUid(new FUserInfoSimpleResDto(userInfo));
            fBallValuationResDto.setValueUuid(null);
            fBallValuationResDto.setBallDislike(0L);
            fBallValuationResDto.setBallLike(0L);
            fBallValuationResDto.setBallUuid(new FBallResDto(fBall));
            fBallValuationResDto.setPoint(0L);
            return fBallValuationResDto;
        }

    }
}

@Service
@Transactional
@RequiredArgsConstructor
class BallGuestUserValuationSearchImpl implements BallUserValuationSearch {

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallValuationDataRepository fBallValuationDataRepository;

    final FBallDataRepository fBallDataRepository;

    @Override
    public FBallValuationResDto search(String ballUuid, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        FBallValuationResDto fBallValuationResDto = new FBallValuationResDto();
        fBallValuationResDto.setBallDislike(0L);
        fBallValuationResDto.setBallLike(0L);
        fBallValuationResDto.setBallUuid(new FBallResDto(fBall));
        fBallValuationResDto.setPoint(0L);
        return fBallValuationResDto;
    }
}