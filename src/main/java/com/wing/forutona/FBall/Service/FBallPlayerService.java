package com.wing.forutona.FBall.Service;

import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.FBall.Dto.FBallPlayerResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallSelectReqDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerDataRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FBallPlayerService {

    final FBallPlayerQueryRepository fBallPlayerQueryRepository;

    final FBallPlayerDataRepository fBallPlayerDataRepository;

    final FBallDataRepository fBallDataRepository;


    public Page<FBallPlayerResDto> UserToPlayBallList(String playerUid, Pageable pageable){
        Page<FBallPlayer> fBallPlayerByPlayer = fBallPlayerQueryRepository.getUserToPlayBallList(playerUid,pageable);
        return fBallPlayerByPlayer.map(x->new FBallPlayerResDto(x));

    }

}
