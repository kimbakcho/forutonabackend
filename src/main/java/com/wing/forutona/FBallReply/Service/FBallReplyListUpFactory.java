package com.wing.forutona.FBallReply.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FBallReplyListUpFactory {


    final  private FBallReplyRootNodeListUp fBallReplyRootNodeListUp;

    final private FBallReplySubNodeListUp fBallReplySubNodeListUp;

    public  FBallReplyListUpService getListUpService(boolean isOnlySubMode){
        if(isOnlySubMode){
            return fBallReplySubNodeListUp;
        }else {
            return fBallReplyRootNodeListUp;
        }
    }
}
