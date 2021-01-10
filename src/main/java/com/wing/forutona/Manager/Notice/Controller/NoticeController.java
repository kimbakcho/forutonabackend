package com.wing.forutona.Manager.Notice.Controller;

import com.wing.forutona.Manager.Notice.Dto.NoticeResDto;
import com.wing.forutona.Manager.Notice.Service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Notice")
public class NoticeController {

    final NoticeService noticeService;

    @GetMapping("/items")
    public Page<NoticeResDto> getNotices(Pageable pageable){
        return noticeService.getNotices(pageable);
    }


}
