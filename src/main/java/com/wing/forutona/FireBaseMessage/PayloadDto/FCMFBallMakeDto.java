package com.wing.forutona.FireBaseMessage.PayloadDto;

import com.wing.forutona.FBall.Domain.FBallType;
import lombok.Data;

@Data
public class FCMFBallMakeDto {
    String ballMakerNickName;
    String ballMakerProfileImageUrl;
    String ballTitle;
    String ballUuid;
    FBallType fBallType;
}
