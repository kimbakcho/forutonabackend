package com.wing.forutona.ForutonaUser.Service;


import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.*;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Service.SnsLogin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface FUserInfoService {
    FUserInfoResDto selectFUserInfo(String uid);
    void updateUserPosition(UserPositionUpdateReqDto reqDto, FFireBaseToken fireBaseToken);
    void updateFireBaseMessageToken(String uid, String token);
    FUserInfoJoinResDto joinUser(FUserInfoJoinReqDto reqDto) throws Exception;
    FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(FUserSnSLoginReqDto snSLoginReqDto) throws Exception;
}

@Service
@RequiredArgsConstructor
@Transactional
class FUserInfoServiceImpl implements FUserInfoService {

    final FUserInfoDataRepository fUserInfoDataRepository;

    final SnsLoginServiceFactory snsLoginServiceFactory;

    @Override
    public FUserInfoResDto selectFUserInfo(String uid) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();
        return new FUserInfoResDto(fUserInfo);
    }

    @Override
    public void updateUserPosition(UserPositionUpdateReqDto reqDto, FFireBaseToken fireBaseToken) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getUserFireBaseUid()).get();
        fUserInfo.updatePlacePoint(reqDto.getLat(),reqDto.getLng());
    }

    @Override
    public void updateFireBaseMessageToken(String uid, String token) {
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(uid).get();
        fUserInfo.setFCMtoken(token);
    }

    @Override
    public FUserInfoJoinResDto joinUser(FUserInfoJoinReqDto reqDto) throws Exception {
        SnsLoginService snsLoginService = snsLoginServiceFactory.makeService(reqDto.getSnsSupportService());
        return snsLoginService.join(reqDto);
    }

    @Override
    public FUserSnsCheckJoinResDto getSnsUserJoinCheckInfo(FUserSnSLoginReqDto snSLoginReqDto) throws Exception {
        SnsLoginService snsLoginService = snsLoginServiceFactory.makeService(snSLoginReqDto.getSnsService());
        return snsLoginService.getInfoFromToken(snSLoginReqDto);
    }


}
