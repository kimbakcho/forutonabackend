package com.wing.forutona.FireBaseMessage.PayloadDto;

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
