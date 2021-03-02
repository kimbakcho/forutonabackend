package com.wing.forutona.App.FBallValuation.Service;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBallValuation.Dto.FBallValuationResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallValuation.Repositroy.FBallValuationDataRepository;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoSimpleResDto;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        List<FBallValuation> fBallValuations = fBallValuationDataRepository.findByBallUuidIsAndUid(fBall, userInfo);
        if (fBallValuations.size()>0) {
            return new FBallValuationResDto(fBallValuations.get(0));
        } else {
            FBallValuationResDto fBallValuationResDto = new FBallValuationResDto();
            fBallValuationResDto.setValueUuid(null);
            fBallValuationResDto.setBallDislike(0);
            fBallValuationResDto.setBallLike(0);
            fBallValuationResDto.setBallUuid(new FBallResDto(fBall));
            fBallValuationResDto.setPoint(0);
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
        fBallValuationResDto.setBallDislike(0);
        fBallValuationResDto.setBallLike(0);
        fBallValuationResDto.setBallUuid(new FBallResDto(fBall));
        fBallValuationResDto.setPoint(0);
        return fBallValuationResDto;
    }
}