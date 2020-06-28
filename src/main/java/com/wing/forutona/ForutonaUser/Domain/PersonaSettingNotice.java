package com.wing.forutona.ForutonaUser.Domain;

import com.wing.forutona.ForutonaUser.Dto.PersonaSettingNoticeInsertReqDto;
import com.wing.forutona.ForutonaUser.Dto.PersonaSettingNoticeUpdateReqDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


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


    public static PersonaSettingNotice fromPersonaSettingNoticeInsertReqDto (PersonaSettingNoticeInsertReqDto reqDto){
        PersonaSettingNotice personaSettingNotice = new PersonaSettingNotice();
        personaSettingNotice.noticeName = reqDto.getNoticeName();
        personaSettingNotice.noticeWriteDateTime = reqDto.getNoticeWriteDateTime();
        personaSettingNotice.noticeContent = reqDto.getNoticeContent();
        personaSettingNotice.lang = reqDto.getLang();
        return personaSettingNotice;
    }

    public void updatePersonaSettingNotice(PersonaSettingNoticeUpdateReqDto reqDto){
        this.lang = reqDto.getLang();
        this.noticeContent = reqDto.getNoticeContent();
        this.noticeName = reqDto.getNoticeName();
        this.noticeWriteDateTime = reqDto.getNoticeWriteDateTime();
    }

}
