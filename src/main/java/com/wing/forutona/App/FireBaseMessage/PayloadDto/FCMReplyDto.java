package com.wing.forutona.App.FireBaseMessage.PayloadDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wing.forutona.App.FBall.Domain.FBallType;
import lombok.Data;



@Data
public class FCMReplyDto {
    String replyUserUid;
    String nickName;
    String replyText;
    String userProfileImageUrl;
    String ballUuid;
    String replyTitleType;
    @JsonProperty("fBallType")
    FBallType fBallType;
}
