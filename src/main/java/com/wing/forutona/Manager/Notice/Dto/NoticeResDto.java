package com.wing.forutona.Manager.Notice.Dto;

import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import com.wing.forutona.Manager.MUserInfo.Dto.MUserInfoResDto;
import com.wing.forutona.Manager.Notice.Domain.Notice;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class NoticeResDto {
    Integer idx;
    String title;
    String content;
    String openFlag;

    MUserInfoResDto writerUid;

    LocalDateTime modifyDate;

    public static NoticeResDto fromNotice(Notice notice){
        NoticeResDto noticeResDto = new NoticeResDto();
        noticeResDto.idx = notice.getIdx();
        noticeResDto.title = notice.getTitle();
        noticeResDto.content = notice.getContent();
        noticeResDto.openFlag = notice.getOpenFlag();
        noticeResDto.writerUid = MUserInfoResDto.fromUserInfoResDto(notice.getWriterUid());
        noticeResDto.modifyDate = notice.getModifyDate();
        return noticeResDto;
    }
}
