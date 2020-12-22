package com.wing.forutona.App.ForutonaUser.Dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PersonaSettingNoticeUpdateReqDto {
    Long idx;
    String noticeName;
    LocalDateTime noticeWriteDateTime;
    String noticeContent;
    String lang;
}
