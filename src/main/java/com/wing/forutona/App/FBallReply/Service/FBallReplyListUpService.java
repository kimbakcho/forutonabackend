package com.wing.forutona.App.FBallReply.Service;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyReqDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallReply.Repositroy.FBallReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface FBallReplyListUpService {
    Page<FBallReplyResDto> listUpReply(FBallReplyReqDto reqDto, Pageable pageable);
}

@Service
@Transactional
@RequiredArgsConstructor
class FBallReplyRootNodeListUp implements FBallReplyListUpService {

    final FBallReplyQueryRepository fBallReplyQueryRepository;
    final FBallDataRepository fBallDataRepository;

    @Override
    public Page<FBallReplyResDto> listUpReply(FBallReplyReqDto reqDto, Pageable pageable) {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        return fBallReplyQueryRepository.getFBallRootNodeReply(fBall,pageable);
    }
}

@Service
@Transactional
@RequiredArgsConstructor
class FBallReplySubNodeListUp implements FBallReplyListUpService {

    final FBallReplyQueryRepository fBallReplyQueryRepository;
    final FBallDataRepository fBallDataRepository;

    @Override
    public Page<FBallReplyResDto> listUpReply(FBallReplyReqDto reqDto, Pageable pageable) {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        return fBallReplyQueryRepository.getFBallSubNodeReply(fBall,reqDto.getReplyNumber(),pageable);
    }
}
