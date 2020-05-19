package com.wing.forutona.FBall.Service.FBallType;

import com.wing.forutona.FBall.Dto.FBallType;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
@NoArgsConstructor
public class FBallTypeServiceFactory {

    @Autowired
    IssueBallTypeServiceFactory issueBallMakerService;

    public  FBallTypeService getService(FBallType fBallType){
        if(fBallType.equals(FBallType.IssueBall)){
            return issueBallMakerService;
        }else {
            return null;
        }
    }
}


