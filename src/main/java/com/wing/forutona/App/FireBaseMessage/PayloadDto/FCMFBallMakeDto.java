package com.wing.forutona.App.FireBaseMessage.PayloadDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wing.forutona.App.FBall.Domain.FBallType;
import lombok.Data;

@Data
public class FCMFBallMakeDto {
    String ballMakerNickName;
    String ballMakerProfileImageUrl;
    String ballTitle;
    String ballUuid;
    @JsonProperty("fBallType")
    FBallType fBallType;
}
