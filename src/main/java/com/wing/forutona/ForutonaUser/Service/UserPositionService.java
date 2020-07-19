package com.wing.forutona.ForutonaUser.Service;

import com.google.type.LatLng;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.UserPositionUpdateReqDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface UserPositionService {
    int updateUserPosition(UserPositionUpdateReqDto reqDto, FFireBaseToken fireBaseToken);
}

@Service
class UserPositionServiceImpl implements  UserPositionService{

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Override
    @Transactional
    public int updateUserPosition(UserPositionUpdateReqDto reqDto, FFireBaseToken fireBaseToken) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();
        fUserInfo.updatePlacePoint(reqDto.getLat(),reqDto.getLng());
        return  1;
    }

}