package com.wing.forutona.PersonaSettingNotice.Dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 추후 관리자 프로젝트에서 데이터를 집어 넣을것이지만 혹시 몰라서 코드는 작성 해둠.
 */
@Data
public class PersonaSettingNoticeInsertReqDto {
    Long idx;
    String noticeName;
    LocalDateTime noticeWriteDateTime;
    String noticeContent;
    String lang;

}
