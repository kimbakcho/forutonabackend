package com.wing.forutona.Manager.MaliciousReply.Controller;

import com.wing.forutona.Manager.MaliciousReply.Dto.MaliciousReplyReqDto;
import com.wing.forutona.Manager.MaliciousReply.Dto.MaliciousReplyResDto;
import com.wing.forutona.Manager.MaliciousReply.Service.MaliciousReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("MaliciousReply")
@RequiredArgsConstructor
public class MaliciousReplyController {

    final MaliciousReplyService maliciousReplyService;

    @PostMapping
    public MaliciousReplyResDto reportMaliciousReply(MaliciousReplyReqDto reqDto){
        return maliciousReplyService.reportMaliciousReply(reqDto);
    }

}
