package com.wing.forutona.App.FireBaseMessage.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FireBaseMessageSendDto<T> {
    String serviceKey;
    String isNotification;
    String fcmToken;
    T payLoad;

    @Builder
    FireBaseMessageSendDto(String serviceKey,String fcmToken,boolean isNotification,T payLoad){
        this.serviceKey = serviceKey;
        this.isNotification = isNotification ? "true" : "false";
        this.payLoad = payLoad;
        this.fcmToken = fcmToken;
    }
}
