package com.wing.forutona.FBall.Service;

import com.wing.forutona.CustomUtil.FSorts;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResWrapDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallSelectReqDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerDataRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.RequiredArgsConstructor;
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
public class FBallPlayerService {

    final FBallPlayerQueryRepository fBallPlayerQueryRepository;

    final FBallPlayerDataRepository fBallPlayerDataRepository;

    final FBallDataRepository fBallDataRepository;

    @Async
    @Transactional
    public void UserToPlayBallList(ResponseBodyEmitter emitter, UserToPlayBallReqDto reqDto, FSorts sorts, Pageable pageable){
        try {
            List<UserToPlayBallResDto> fBallPlayerByPlayer = fBallPlayerQueryRepository.getUserToPlayBallList(reqDto,sorts, pageable);
            UserToPlayBallResWrapDto userToPlayBallResWrapDto = new UserToPlayBallResWrapDto(LocalDateTime.now(),fBallPlayerByPlayer);
            emitter.send(userToPlayBallResWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void UserToPlayBall(ResponseBodyEmitter emitter, UserToPlayBallSelectReqDto reqDto) {
        try{
            FUserInfo fUserInfo = FUserInfo.builder().uid(reqDto.getPlayerUid()).build();
            FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
            FBallPlayer fBallPlayer = fBallPlayerDataRepository.findFBallPlayerByPlayerUidIsAndBallUuidIs(fUserInfo, fBall);
            UserToPlayBallResDto userToPlayBallResDto = new UserToPlayBallResDto(fBallPlayer);
            emitter.send(userToPlayBallResDto);
        }catch (IOException e){
            e.printStackTrace();;
        }finally {
            emitter.complete();
        }
    }
}
