package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.Fcubeplayer;
import com.wing.forutona.FcubeDto.FcubequestsuccessExtender1;
import com.wing.forutona.FcubeDto.Fcubereply;
import com.wing.forutona.FcubeDto.Fcubereview;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

@Transactional
public interface FcubeDaoInter {
    void InsertCubeReply(Fcubereply reply, ResponseBodyEmitter emitter) throws  Exception;
    int insertFcubePlayer(Fcubeplayer fcubeplayer);
    void updateQuestReq(ResponseBodyEmitter emitter, FcubequestsuccessExtender1 item);
    void insertFcubeReviewExpPoint(ResponseBodyEmitter emitter, Fcubereview item) throws IOException;
    void updateCubeHitPoint(ResponseBodyEmitter emitter,String cubeuuid);
}
