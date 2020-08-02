package com.wing.forutona.FBall.Service.FBallReply;

import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
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

    @Override
    public Page<FBallReplyResDto> listUpReply(FBallReplyReqDto reqDto, Pageable pageable) {
        return fBallReplyQueryRepository.getFBallRootNodeReply(reqDto,pageable);
    }
}

@Service
@Transactional
@RequiredArgsConstructor
class FBallReplySubNodeListUp implements FBallReplyListUpService {

    final FBallReplyQueryRepository fBallReplyQueryRepository;

    @Override
    public Page<FBallReplyResDto> listUpReply(FBallReplyReqDto reqDto, Pageable pageable) {
        return fBallReplyQueryRepository.getFBallSubNodeReply(reqDto,pageable);
    }
}
