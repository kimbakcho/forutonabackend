package com.wing.forutona.PersonaSettingNotice.Dto;

import com.wing.forutona.PersonaSettingNotice.Domain.PersonaSettingNotice;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonaSettingNoticeResDto {

    Long idx;
    String noticeName;
    LocalDateTime noticeWriteDateTime;
    String noticeContent;
    String lang;


    public PersonaSettingNoticeResDto(PersonaSettingNotice item){
        this.idx = item.getIdx();
        this.noticeName = item.getNoticeName();
        this.noticeWriteDateTime = item.getNoticeWriteDateTime();
        this.noticeContent = item.getNoticeContent();
        this.lang = item.getLang();
    }

}
