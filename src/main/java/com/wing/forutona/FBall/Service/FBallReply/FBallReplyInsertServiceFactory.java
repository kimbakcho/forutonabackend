package com.wing.forutona.FBall.Service.FBallReply;

import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FBallReplyInsertServiceFactory {

    final
    FBallReplyInsertService fBallReplyRootInsertServiceImpl;

    final
    FBallReplyInsertService fBallReplySubInsertService;

    public FBallReplyInsertServiceFactory(@Qualifier("FBallReplyRootInsertService") FBallReplyInsertService fBallReplyRootInsertServiceImpl,
                                          @Qualifier("FBallReplySubInsertService") FBallReplyInsertService fBallReplySubInsertService) {
        this.fBallReplyRootInsertServiceImpl = fBallReplyRootInsertServiceImpl;
        this.fBallReplySubInsertService = fBallReplySubInsertService;
    }

    public FBallReplyInsertService getFBallReplyInsertServiceFactory(FBallReplyInsertReqDto reqDto) {
        if (isRootReply(reqDto)) {
            return fBallReplyRootInsertServiceImpl;
        } else {
            return fBallReplySubInsertService;
        }
    }

    public boolean isRootReply(FBallReplyInsertReqDto reqDto) {
        return reqDto.getReplyNumber() == -1;
    }

}
