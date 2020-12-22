package com.wing.forutona.App.FBallReply.Service;

import com.wing.forutona.App.FBallReply.Dto.FBallReplyInsertReqDto;
import lombok.RequiredArgsConstructor;
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
