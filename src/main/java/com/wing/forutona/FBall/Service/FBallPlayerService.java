package com.wing.forutona.FBall.Service;

import com.querydsl.core.QueryResults;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.FBall.Dto.UserToPlayBallReqDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResDto;
import com.wing.forutona.FBall.Dto.UserToPlayBallResWrapDto;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerDataRepositroy;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerQueryRepository;
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
public class FBallPlayerService {

    @Autowired
    FBallPlayerQueryRepository fBallPlayerQueryRepository;

    @Async
    @Transactional
    public void UserToPlayBallList(ResponseBodyEmitter emitter,UserToPlayBallReqDto reqDto, Pageable pageable){
        try {
            List<UserToPlayBallResDto> fBallPlayerByPlayer = fBallPlayerQueryRepository.findFBallPlayerByPlayer(reqDto, pageable);
            UserToPlayBallResWrapDto userToPlayBallResWrapDto = new UserToPlayBallResWrapDto(LocalDateTime.now(),fBallPlayerByPlayer);
            emitter.send(userToPlayBallResWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
