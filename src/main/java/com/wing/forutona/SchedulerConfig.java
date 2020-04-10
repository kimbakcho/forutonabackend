package com.wing.forutona;

import com.wing.forutona.FcubeDao.FcubeMapper;
import com.wing.forutona.FcubeDto.Fcube;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.ListIterator;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private final int POOL_SIZE = 10;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {


        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();

        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    //한동안 꺼둠
    @Profile("none")
    @Component
    public static class FcubeScheduled {

        @Autowired
        FcubeScheduledSupport1 fcubeScheduledSupport1;

        private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

        @Resource(name = "sqlSession")
        private SqlSession sqlSession;


//        @Scheduled(fixedDelay = 10000)
//        public void FcubeFinishScheduled(){
//            FcubeMapper fcubeMapper =  sqlSession.getMapper(FcubeMapper.class);
//            List<Fcube> findcubelist= fcubeMapper.finddonotfinishcube();
//            ListIterator<Fcube> listitr = findcubelist.listIterator();
//            while(listitr.hasNext()){
//                Fcube tempcube = listitr.next();
//                fcubeScheduledSupport1.FcubeFinishExecute(tempcube);
//            }
//        }
    }
}
