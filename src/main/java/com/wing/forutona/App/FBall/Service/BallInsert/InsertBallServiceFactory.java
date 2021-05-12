package com.wing.forutona.App.FBall.Service.BallInsert;

import com.wing.forutona.App.FBall.Domain.FBallType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InsertBallServiceFactory {

    @Autowired
    @Qualifier("BallInsertServiceImpl")
    BallInsertService ballInsertService;

    @Autowired
    @Qualifier("QuestBallInsertServiceImpl")
    BallInsertService questBallInsertService;

    public BallInsertService getBallInsertService(FBallType fBallType){
        if(fBallType.equals(FBallType.IssueBall)){
            return ballInsertService;
        }else if(fBallType.equals(FBallType.QuestBall)){
            return questBallInsertService;
        }else {
            return null;
        }
    }
}
