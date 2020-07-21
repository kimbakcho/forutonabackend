package com.wing.forutona.FireBaseMessage.Service;

import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FBallReplyFCMServiceFactory {

    final
    FBallReplyFCMService fBallReplyRootInsertService;

    final
    FBallReplyFCMService fBallReplySubInsertService;


    public FBallReplyFCMServiceFactory(@Qualifier("FBallRootReplyFCMService") FBallReplyFCMService fBallReplyRootInsertService
            ,@Qualifier("FBallSubReplyFCMService") FBallReplyFCMService fBallReplySubInsertService) {
        this.fBallReplyRootInsertService = fBallReplyRootInsertService;
        this.fBallReplySubInsertService = fBallReplySubInsertService;
    }

    public FBallReplyFCMService getFBallReplyFCMServiceFactory(FBallReplyInsertReqDto reqDto){
        if(isRootReply(reqDto)){
            return fBallReplyRootInsertService;
        }else {
            return fBallReplySubInsertService;
        }
    }
    public boolean isRootReply(FBallReplyInsertReqDto reqDto) {
        return reqDto.getReplyNumber() == -1;
    }
}
