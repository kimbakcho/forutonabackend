package com.wing.forutona.ForutonaUser.Domain;

import com.wing.forutona.ForutonaUser.Dto.PersonaSettingNoticeInsertReqDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PersonaSettingNotice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idx;
    String noticeName;
    LocalDateTime noticeWriteDateTime;
    String noticeContent;
    String lang;

    public PersonaSettingNotice(PersonaSettingNoticeInsertReqDto reqDto){
        this.noticeName = reqDto.getNoticeName();
        this.noticeWriteDateTime = reqDto.getNoticeWriteDateTime();
        this.noticeContent = reqDto.getNoticeContent();
        this.lang = reqDto.getLang();
    }

}
