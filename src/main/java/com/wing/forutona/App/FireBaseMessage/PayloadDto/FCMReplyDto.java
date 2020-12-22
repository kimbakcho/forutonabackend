package com.wing.forutona.App.FireBaseMessage.PayloadDto;

import lombok.Data;



@Data
public class FCMReplyDto {
    String replyUserUid;
    String nickName;
    String replyText;
    String userProfileImageUrl;
    String ballUuid;
    String replyTitleType;
}
