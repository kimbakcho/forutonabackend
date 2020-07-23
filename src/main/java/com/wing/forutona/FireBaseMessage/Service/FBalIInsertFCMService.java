package com.wing.forutona.FireBaseMessage.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallType;
import com.wing.forutona.FireBaseMessage.Dto.FireBaseMessageSendDto;
import com.wing.forutona.FireBaseMessage.PayloadDto.FCMFBallMakeDto;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FBalIInsertFCMService {
    void sendInsertFCMMessage(FBall fBall) throws ParseException;
}

@Component
class FBalIInsertFCMServiceFactory {

    final IssueFBalIInsertFCMServiceImpl issueFBalIInsertFCMService;

    FBalIInsertFCMServiceFactory(
            @Qualifier("IssueFBalIInsertFCMService") IssueFBalIInsertFCMServiceImpl issueFBalIInsertFCMService) {
        this.issueFBalIInsertFCMService = issueFBalIInsertFCMService;
    }

    FBalIInsertFCMService getService(FBallType fBallType) {
        if(fBallType.equals(FBallType.IssueBall)){
            return issueFBalIInsertFCMService;
        }else {
            return null;
        }
    }
}

@Service("IssueFBalIInsertFCMService")
@RequiredArgsConstructor
class IssueFBalIInsertFCMServiceImpl implements FBalIInsertFCMService {

    final FUserInfoQueryRepository fUserInfoQueryRepository;

    final FireBaseMessageAdapter fireBaseMessageAdapter;

    @Override
    public void sendInsertFCMMessage(FBall fBall) throws ParseException {
        LatLng latLng = LatLng.newBuilder()
                .setLatitude(fBall.getLatitude())
                .setLongitude(fBall.getLongitude())
                .build();
        List<FUserInfoResDto> findNearUserFromGeoLocation =
                fUserInfoQueryRepository.getFindNearUserFromGeoLocation(latLng, 1500);

        FCMFBallMakeDto fcmfBallMakeDto = new FCMFBallMakeDto();
        fcmfBallMakeDto.setBallMakerNickName(fBall.getMakerNickName());
        fcmfBallMakeDto.setBallMakerProfileImageUrl(fBall.getMakerProfileImage());
        fcmfBallMakeDto.setBallTitle(fBall.getBallName());
        fcmfBallMakeDto.setBallUuid(fBall.getBallUuid());

        findNearUserFromGeoLocation.forEach(x -> {
            FireBaseMessageSendDto fireBaseMessageSendDto = FireBaseMessageSendDto.builder()
                    .commandKey("RadarBasicChannelUseCase")
                    .serviceKey("IssueFBalIInsertFCMService")
                    .isNotification(true)
                    .fcmToken(x.getFCMtoken())
                    .payLoad(fcmfBallMakeDto)
                    .build();
            try {
                fireBaseMessageAdapter.sendMessage(fireBaseMessageSendDto);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        });

    }
}