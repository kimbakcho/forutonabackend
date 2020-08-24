package com.wing.forutona.FBallReply.Service;

import com.wing.forutona.FBallReply.Dto.FBallReplyInsertReqDto;
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
