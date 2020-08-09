package com.wing.forutona.FBall.Service.BallValuation;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BallUserValuationSearchFactory {

    final FUserInfoDataRepository fUserInfoDataRepository;
    final BallSignUserValuationSearchImpl ballSignUserValuationSearch;
    final  BallGuestUserValuationSearchImpl ballGuestUserValuationSearch;

    public BallUserValuationSearch getSearchEngine(String userUid){
        Optional<FUserInfo> optionalFUserInfo = fUserInfoDataRepository.findById(userUid);
        if(optionalFUserInfo.isPresent()){
            return ballSignUserValuationSearch;
        }else {
            return ballGuestUserValuationSearch;
        }
    }
}
