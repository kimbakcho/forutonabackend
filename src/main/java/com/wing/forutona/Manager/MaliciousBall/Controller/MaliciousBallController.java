package com.wing.forutona.Manager.MaliciousBall.Controller;

import com.wing.forutona.Manager.MaliciousBall.Dto.MaliciousBallReqDto;
import com.wing.forutona.Manager.MaliciousBall.Dto.MaliciousBallResDto;
import com.wing.forutona.Manager.MaliciousBall.Service.MaliciousBallService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("MaliciousBall")
@RequiredArgsConstructor
public class MaliciousBallController {

    final MaliciousBallService maliciousReplyService;

    @PostMapping
    public MaliciousBallResDto reportMaliciousBall(MaliciousBallReqDto reqDto){
        return maliciousReplyService.reportMaliciousBall(reqDto);
    }


}
