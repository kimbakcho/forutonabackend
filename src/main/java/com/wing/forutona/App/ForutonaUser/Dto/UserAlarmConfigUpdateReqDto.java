package com.wing.forutona.App.ForutonaUser.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


public class UserAlarmConfigUpdateReqDto {

    Boolean alarmChatMessage;

    Boolean alarmContentReply;

    Boolean alarmReplyAndReply;

    Boolean alarmFollowNewContent;

    Boolean alarmSponNewContent;

    public UserAlarmConfigUpdateReqDto(Boolean alarmChatMessage, Boolean alarmContentReply, Boolean alarmReplyAndReply, Boolean alarmFollowNewContent, Boolean alarmSponNewContent) {
        this.alarmChatMessage = alarmChatMessage;
        this.alarmContentReply = alarmContentReply;
        this.alarmReplyAndReply = alarmReplyAndReply;
        this.alarmFollowNewContent = alarmFollowNewContent;
        this.alarmSponNewContent = alarmSponNewContent;
    }

    public Boolean getAlarmChatMessage() {
        return alarmChatMessage;
    }

    public void setAlarmChatMessage(Boolean alarmChatMessage) {
        this.alarmChatMessage = alarmChatMessage;
    }

    public Boolean getAlarmContentReply() {
        return alarmContentReply;
    }

    public void setAlarmContentReply(Boolean alarmContentReply) {
        this.alarmContentReply = alarmContentReply;
    }

    public Boolean getAlarmReplyAndReply() {
        return alarmReplyAndReply;
    }

    public void setAlarmReplyAndReply(Boolean alarmReplyAndReply) {
        this.alarmReplyAndReply = alarmReplyAndReply;
    }

    public Boolean getAlarmFollowNewContent() {
        return alarmFollowNewContent;
    }

    public void setAlarmFollowNewContent(Boolean alarmFollowNewContent) {
        this.alarmFollowNewContent = alarmFollowNewContent;
    }

    public Boolean getAlarmSponNewContent() {
        return alarmSponNewContent;
    }

    public void setAlarmSponNewContent(Boolean alarmSponNewContent) {
        this.alarmSponNewContent = alarmSponNewContent;
    }
}
