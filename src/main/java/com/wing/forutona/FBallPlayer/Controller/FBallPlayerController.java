package com.wing.forutona.FBallPlayer.Controller;

import com.wing.forutona.FBallPlayer.Dto.FBallPlayerResDto;
import com.wing.forutona.FBallPlayer.Service.FBallPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FBallPlayerController {

    final FBallPlayerService fBallPlayerService;

    @GetMapping(value = "/v1/FBallPlayer/UserToPlayBallList")
    public Page<FBallPlayerResDto> UserToPlayBallList(String playerUid, Pageable pageable){
        return fBallPlayerService.UserToPlayBallList(playerUid,pageable);
    }

}
