package com.wing.forutona;

import com.wing.forutona.AuthDao.UserexppointhistroyMapper;
import com.wing.forutona.AuthDao.UserinfoMapper;
import com.wing.forutona.AuthDto.Userexppointhistroy;
import com.wing.forutona.AuthDto.Userinfo;
import com.wing.forutona.FcubeDao.FcubeMapper;
import com.wing.forutona.FcubeDto.Fcube;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class FcubeScheduledSupport1 implements FcubeScheduledSupport1In {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;


    public void FcubeFinishExecute(Fcube item){
        logger.info("Thread name = " +  Thread.currentThread().getName());
        fcubeupdateandhistorysave(item);
    }

    @Transactional
    @Override
    public void fcubeupdateandhistorysave(Fcube item){
        FcubeMapper fcubeMapper =  sqlSession.getMapper(FcubeMapper.class);
        Fcube executeitem = fcubeMapper.miniselectforupdate(item.getCubeuuid());
        if(executeitem.getCubestate() != 2){
            executeitem.setCubestate(2);
        }
        if(executeitem.getExpgiveflag() == 0){
            UserinfoMapper userinfoMapper = sqlSession.getMapper(UserinfoMapper.class);
            Userinfo userinfo = userinfoMapper.selectforupdate(executeitem.getUid());
            giveExppoint(executeitem.getCubeuuid(),"Forutona",
                    executeitem.getUid(),"makeExp",executeitem.getMakeexp());
            if(executeitem.getCubetype().equals("issuecube")){
                giveExppoint(executeitem.getCubeuuid(),"Forutona",
                        executeitem.getUid(),"valuationExp",executeitem.getCubelikes() - executeitem.getCubedislikes());

            }
            executeitem.setExpgiveflag(1);
            fcubeMapper.updateCubeState(executeitem);
        }
    }

    int giveExppoint(String cubeuuid,String fromUid,String touid,String explains,double points){
        UserinfoMapper userinfoMapper = sqlSession.getMapper(UserinfoMapper.class);
        Userinfo userinfo = userinfoMapper.selectforupdate(touid);
        userinfo.setExppoint(userinfo.getExppoint() + points);
        userinfoMapper.updateUserExpPoint(userinfo);
        UserexppointhistroyMapper userexppointhistroyMapper =  sqlSession.getMapper(UserexppointhistroyMapper.class);
        Userexppointhistroy recode = new Userexppointhistroy();
        recode.setCubeuuid(cubeuuid);
        recode.setFromuid(fromUid);
        recode.setExplains(explains);
        recode.setPoints(points);
        recode.setUid(userinfo.getUid());
        recode.setGettime(new Date());
        return userexppointhistroyMapper.insert(recode);
    }


    @Transactional
    public void Userexppointupdate(Fcube executeitem){

    }
}


