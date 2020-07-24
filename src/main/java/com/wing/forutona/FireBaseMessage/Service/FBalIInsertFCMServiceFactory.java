package com.wing.forutona.FireBaseMessage.Service;

import com.wing.forutona.FBall.Dto.FBallType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FBalIInsertFCMServiceFactory {
    final IssueFBallInsertFCMServiceImpl issueFBalIInsertFCMService;

    FBalIInsertFCMServiceFactory(
            @Qualifier("IssueFBalIInsertFCMService") IssueFBallInsertFCMServiceImpl issueFBalIInsertFCMService) {
        this.issueFBalIInsertFCMService = issueFBalIInsertFCMService;
    }

    public FBallInsertFCMService getService(FBallType fBallType) {
        if(fBallType.equals(FBallType.IssueBall)){
            return issueFBalIInsertFCMService;
        }else {
            return null;
        }
    }
}
