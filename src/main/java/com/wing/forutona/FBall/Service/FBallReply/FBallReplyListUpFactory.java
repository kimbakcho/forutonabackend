package com.wing.forutona.FBall.Service.FBallReply;

import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import lombok.NoArgsConstructor;
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
