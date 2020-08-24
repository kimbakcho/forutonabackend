package com.wing.forutona.FBallPlayer.Service;

import com.wing.forutona.FBallPlayer.Domain.FBallPlayer;
import com.wing.forutona.FBallPlayer.Dto.FBallPlayerResDto;
import com.wing.forutona.FBallPlayer.Repository.FBallPlayerDataRepository;
import com.wing.forutona.FBallPlayer.Repository.FBallPlayerQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FBallPlayerService {

    final FBallPlayerQueryRepository fBallPlayerQueryRepository;

    final FBallPlayerDataRepository fBallPlayerDataRepository;


    public Page<FBallPlayerResDto> UserToPlayBallList(String playerUid, Pageable pageable){
        Page<FBallPlayer> fBallPlayerByPlayer = fBallPlayerQueryRepository.getUserToPlayBallList(playerUid,pageable);
        return fBallPlayerByPlayer.map(x->new FBallPlayerResDto(x));

    }

}
