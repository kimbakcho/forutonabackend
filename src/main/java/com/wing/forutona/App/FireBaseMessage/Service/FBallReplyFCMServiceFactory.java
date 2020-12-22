package com.wing.forutona.App.FireBaseMessage.Service;

import com.wing.forutona.App.FBallReply.Dto.FBallReplyInsertReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FBallReplyFCMServiceFactory {

    final
    FBallRootReplyFCMService fBallReplyRootInsertService;

    final
    FBallSubReplyFCMService fBallReplySubInsertService;


    public FBallReplyFCMService getFBallReplyFCMServiceFactory(FBallReplyInsertReqDto reqDto) {
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
