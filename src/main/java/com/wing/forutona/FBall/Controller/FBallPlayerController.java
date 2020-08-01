package com.wing.forutona.FBall.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.FBall.Dto.FBallPlayerResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallSelectReqDto;
import com.wing.forutona.FBall.Service.FBallPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequiredArgsConstructor
public class FBallPlayerController {

    final FBallPlayerService fBallPlayerService;

    @GetMapping(value = "/v1/FBallPlayer/UserToPlayBallList")
    public Page<FBallPlayerResDto> UserToPlayBallList(String playerUid, Pageable pageable){
        return fBallPlayerService.UserToPlayBallList(playerUid,pageable);
    }

}
