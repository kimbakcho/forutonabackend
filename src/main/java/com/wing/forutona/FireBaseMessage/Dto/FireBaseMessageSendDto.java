package com.wing.forutona.FireBaseMessage.Dto;

import com.wing.forutona.FireBaseMessage.Service.FireBaseMessageKey;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class FireBaseMessageSendDto<T> {
    String commandKey;
    String serviceKey;
    String isNotification;
    String fcmToken;
    T payLoad;

    @Builder
    FireBaseMessageSendDto(String commandKey,String serviceKey,String fcmToken,boolean isNotification,T payLoad){
        this.commandKey = commandKey;
        this.serviceKey = serviceKey;
        this.isNotification = isNotification ? "true" : "false";
        this.payLoad = payLoad;
        this.fcmToken = fcmToken;
    }
}
