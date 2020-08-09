package com.wing.forutona.FBall.Service.FBallReply;

import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FBallReplyInsertServiceFactory {

    final FBallReplyRootInsertServiceImpl fBallReplyRootInsertService;

    final  FBallReplySubInsertServiceImpl fBallReplySubInsertService;


    public FBallReplyInsertService getFBallReplyInsertServiceFactory(FBallReplyInsertReqDto reqDto) {
        if (isRootReply(reqDto)) {
            return fBallReplyRootInsertService;
        } else {
            return fBallReplySubInsertService;
        }
    }

    public boolean isRootReply(FBallReplyInsertReqDto reqDto) {
        return reqDto.getReplyUuid() == null;
    }

}
