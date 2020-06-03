package com.wing.forutona.FBall.Service;

import com.querydsl.core.QueryResults;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResWrapDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallSelectReqDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerDataRepositroy;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    final FBallPlayerDataRepositroy fBallPlayerDataRepositroy;

    final FBallDataRepository fBallDataRepository;

    @Async
    @Transactional
    public void UserToPlayBallList(ResponseBodyEmitter emitter, UserToPlayBallReqDto reqDto, MultiSorts sorts, Pageable pageable){
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
            FUserInfo fUserInfo = new FUserInfo();
            fUserInfo.setUid(reqDto.getPlayerUid());
            FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
            FBallPlayer fBallPlayer = fBallPlayerDataRepositroy.findFBallPlayerByPlayerUidIsAndBallUuidIs(fUserInfo, fBall);
            UserToPlayBallResDto userToPlayBallResDto = new UserToPlayBallResDto(fBall,fBallPlayer);
            emitter.send(userToPlayBallResDto);
        }catch (IOException e){
            e.printStackTrace();;
        }finally {
            emitter.complete();
        }
    }
}
