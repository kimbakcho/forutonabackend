package com.wing.forutona.FBall.Service.FBallReply;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import com.wing.forutona.FireBaseMessage.Service.FBallReplyFCMService;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface FBallReplyInsertService {
    FBallReply insertReply(FFireBaseToken fireBaseToken, FBallReplyInsertReqDto reqDto,FBallReply saveFBallReplyItem) throws FirebaseMessagingException, JsonProcessingException;
}


@Service
@RequiredArgsConstructor
@Transactional
class FBallReplyRootInsertServiceImpl implements FBallReplyInsertService{

    final FBallReplyQueryRepository fBallReplyQueryRepository;

    @Override
    public FBallReply insertReply(FFireBaseToken fireBaseToken, FBallReplyInsertReqDto reqDto,FBallReply saveFBallReplyItem) throws FirebaseMessagingException, JsonProcessingException {
        Long maxReplyNumber = fBallReplyQueryRepository.getMaxReplyNumber(reqDto.getBallUuid());
        long nextReplyNumber = maxReplyNumber + 1;
        saveFBallReplyItem.setReplyNumber(nextReplyNumber);
        saveFBallReplyItem.setReplySort(0);
        saveFBallReplyItem.addFBallReplyCount();
        return saveFBallReplyItem;
    }
}

@Service
@RequiredArgsConstructor
@Transactional
class FBallReplySubInsertServiceImpl implements FBallReplyInsertService{
    final FBallReplyQueryRepository fBallReplyQueryRepository;
    final FBallReplyDataRepository fBallReplyDataRepository;
    @Override
    public FBallReply insertReply(FFireBaseToken fireBaseToken, FBallReplyInsertReqDto reqDto, FBallReply saveFBallReplyItem) {
        Long maxSortNumber = fBallReplyQueryRepository.getMaxSortNumber(reqDto.getReplyUuid());
        FBallReply fBallReply = fBallReplyDataRepository.findById(reqDto.getReplyUuid()).get();
        saveFBallReplyItem.setReplyNumber(fBallReply.getReplyNumber());
        long nextSortNumber = maxSortNumber + 1;
        saveFBallReplyItem.setReplySort(nextSortNumber);
        return saveFBallReplyItem;
    }
}
