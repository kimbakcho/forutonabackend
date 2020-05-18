package com.wing.forutona.FBall.Service.BallMaker;

import com.wing.forutona.FBall.Dto.FBallType;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FBallMakerFactory {

    @Autowired
    IssueBallMakerService issueBallMakerService;

    public FBallMakerService getService(FBallType fBallType){
        if(fBallType.equals(FBallType.IssueBall)){
            return issueBallMakerService;
        }else {
            return null;
        }

    }
}
