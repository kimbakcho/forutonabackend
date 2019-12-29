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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;



@Component
public class FcubeScheduled {

    @Autowired
    FcubeScheduledSupport1 fcubeScheduledSupport1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Scheduled(fixedDelay = 10000)
    public void FcubeFinishScheduled(){
        FcubeMapper fcubeMapper =  sqlSession.getMapper(FcubeMapper.class);
        List<Fcube> findcubelist= fcubeMapper.finddonotfinishcube();
        ListIterator<Fcube> listitr = findcubelist.listIterator();
        while(listitr.hasNext()){
            Fcube tempcube = listitr.next();
            fcubeScheduledSupport1.FcubeFinishExecute(tempcube);
        }
    }



}
