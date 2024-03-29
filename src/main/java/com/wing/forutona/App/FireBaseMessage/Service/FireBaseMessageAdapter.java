package com.wing.forutona.App.FireBaseMessage.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.wing.forutona.App.FireBaseMessage.Dto.FireBaseMessageSendDto;
import org.springframework.stereotype.Component;

public interface FireBaseMessageAdapter {
    String sendMessage(FireBaseMessageSendDto sendDto) throws JsonProcessingException, FirebaseMessagingException;
}
@Component
class FireBaseMessageAdapterImpl implements  FireBaseMessageAdapter {

    @Override
    public String sendMessage(FireBaseMessageSendDto sendDto) throws JsonProcessingException, FirebaseMessagingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDto = objectMapper.writeValueAsString(sendDto.getPayLoad());

        Message message = Message.builder()
                .putData(FireBaseMessageKey.ServiceKey,sendDto.getServiceKey())
                .putData(FireBaseMessageKey.IsNotification,sendDto.getIsNotification())
                .putData("payload",jsonDto)
                .setToken(sendDto.getFcmToken())
                .build();
        String send = FirebaseMessaging.getInstance().send(message);

        return send;
    }
}