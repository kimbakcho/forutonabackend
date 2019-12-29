package com.wing.forutona;

import com.wing.forutona.AuthDao.UserexppointhistroyMapper;
import com.wing.forutona.AuthDao.UserinfoMapper;
import com.wing.forutona.AuthDto.Userexppointhistroy;
import com.wing.forutona.AuthDto.Userinfo;
import com.wing.forutona.FcubeDao.FcubeMapper;
import com.wing.forutona.FcubeDto.Fcube;
import com.wing.forutona.FcubeDto.FcubeState;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class FcubeScheduledSupport1 {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Async
    public void FcubeFinishExecute(Fcube item){
        logger.info("Thread name = " +  Thread.currentThread().getName());
        fcubeupdateandhistorysave(item);
    }
    @Transactional
    public void fcubeupdateandhistorysave(Fcube item){
        FcubeMapper fcubeMapper =  sqlSession.getMapper(FcubeMapper.class);
        Fcube executeitem = fcubeMapper.miniselectforupdate(item.getCubeuuid());
        if(executeitem.getCubestate() != FcubeState.finish){
            executeitem.setCubestate(FcubeState.finish);
        }
        if(executeitem.getExpgiveflag() == 0){
            UserexppointhistroyMapper userexppointhistroyMapper =  sqlSession.getMapper(UserexppointhistroyMapper.class);
            Userexppointhistroy recode = new Userexppointhistroy();
            recode.setCubeuuid(executeitem.getCubeuuid());
            recode.setFromuid("Forutona");
            recode.setExplains("MakeExp");
            recode.setPoints(executeitem.getMakeexp());
            recode.setUid(executeitem.getUid());
            recode.setGettime(new Date());
            userexppointhistroyMapper.insert(recode);
            UserinfoMapper userinfoMapper = sqlSession.getMapper(UserinfoMapper.class);
            Userinfo userinfo = userinfoMapper.selectforupdate(executeitem.getUid());
            userinfo.setExppoint(userinfo.getExppoint() + executeitem.getMakeexp());
            userinfoMapper.updateUserExpPoint(userinfo);
            executeitem.setExpgiveflag(1);
        }
        fcubeMapper.updateCubeState(executeitem);
    }

    @Transactional
    public void Userexppointupdate(Fcube executeitem){

    }
}


