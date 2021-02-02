package com.wing.forutona.Manager.Notice.Service;

import com.wing.forutona.Manager.Notice.Domain.Notice;
import com.wing.forutona.Manager.Notice.Dto.NoticeResDto;
import com.wing.forutona.Manager.Notice.Repositroy.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    final NoticeRepository noticeRepository;

    public Page<NoticeResDto> getNotices(Pageable pageable) {
        Page<Notice> all = noticeRepository.findAll(pageable);
        Page<NoticeResDto> result = all.map(x -> NoticeResDto.fromNotice(x));
        return result;
    }

    public NoticeResDto getNotice(Integer idx) {
        Notice notice = noticeRepository.findById(idx).get();
        return NoticeResDto.fromNotice(notice);
    }

}
