package com.wing.forutona.ForutonaUser.Service;

import com.wing.forutona.ForutonaUser.Dto.FUserInfoDto;
import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoQueryRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FUserInfoService {
    @Autowired
    FUserInfoQueryRepository fUserInfoQueryRepository;

    @Transactional
    public FUserInfoDto getBasicUserInfo(FUserReqDto fUserReqDto){
        return fUserInfoQueryRepository.getBasicUserInfo(fUserReqDto);
    }
}
