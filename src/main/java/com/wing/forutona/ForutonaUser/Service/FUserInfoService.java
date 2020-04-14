package com.wing.forutona.ForutonaUser.Service;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.ForutonaUser.Dto.FUserReqDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

@Service
public class FUserInfoService {
    @Autowired
    FUserInfoQueryRepository fUserInfoQueryRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Transactional
    public FUserInfoResDto getBasicUserInfo(FUserReqDto fUserReqDto){
        return fUserInfoQueryRepository.getBasicUserInfo(fUserReqDto);
    }

    @Async
    @Transactional
    public void getMe(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken){
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getFireBaseToken().getUid()).get();
        try {
            emitter.send(new FUserInfoResDto(fUserInfo));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
