package com.wing.forutona.FBall.Service;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.FBallReplyInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.FBallReplyResWrapDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyDataRepository;
import com.wing.forutona.FBall.Repository.FBallReply.FBallReplyQueryRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FBallReplyService {

    @Autowired
    FBallReplyDataRepository fBallReplyDataRepository;

    @Autowired
    FBallReplyQueryRepository fBallReplyQueryRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;


    @Async
    @Transactional
    public void insertFBallReply(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken, FBallReplyInsertReqDto reqDto) {
        FBallReply fBallReply = new FBallReply();
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        fBallReply.setReplyBallUuid(fBall);
        //대댓글이 아닌 처음 댓글
        if(reqDto.getReplyNumber() == -1){
            FBallReply top1ByReplyBallUuidIsOrderByReplyNumberDesc = fBallReplyDataRepository.findTop1ByReplyBallUuidIsOrderByReplyNumberDesc(fBall);
            fBallReply.setReplyNumber(0L);
            if(top1ByReplyBallUuidIsOrderByReplyNumberDesc == null ){
                fBallReply.setReplyNumber(0L);
            }else {
                fBallReply.setReplyNumber(top1ByReplyBallUuidIsOrderByReplyNumberDesc.getReplyNumber()+1);
            }
            fBallReply.setReplySort(0L);
            fBallReply.setReplyDepth(0L);
        }else {
            fBallReplyQueryRepository.updateReplySortPlusOne(fBall,reqDto.getReplyNumber(),reqDto.getReplySort());
            fBallReply.setReplySort(reqDto.getReplySort());
            fBallReply.setReplyDepth(1L);
            fBallReply.setReplyNumber(reqDto.getReplyNumber());
        }
        FUserInfo fUserInfo = new FUserInfo();
        fUserInfo.setUid(fireBaseToken.getFireBaseToken().getUid());
        fBallReply.setReplyUid(fUserInfo);
        fBallReply.setReplyText(reqDto.getReplyText());
        fBallReply.setReplyUploadDateTime(LocalDateTime.now());
        fBallReplyDataRepository.save(fBallReply);
        try {
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    /**
     * 조회 할때 처음 대표 댓글만 가져오는 부분.
     * replyDepth 을 0으로 해서 가져옴 대표 댓글의 댓글은 가져오지 않음.
     * @param emitter
     * @param reqDto
     * @param pageable
     */
    @Async
    @Transactional
    public void getFBallReply(ResponseBodyEmitter emitter, FBallReplyReqDto reqDto, Pageable pageable) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        List<FBallReplyResDto> fBallReply = fBallReplyQueryRepository.getFBallReply(fBall, pageable);
        FBallReplyResWrapDto fBallReplyResWrapDto = new FBallReplyResWrapDto();
        fBallReplyResWrapDto.setContents(fBallReply);
        fBallReplyResWrapDto.setCount(fBallReply.size());
        try {
            emitter.send(fBallReplyResWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getFBallDetailReply(ResponseBodyEmitter emitter, FBallReplyReqDto reqDto) {
        List<FBallReplyResDto> fBallReply = fBallReplyQueryRepository.getFBallDetailReply(reqDto);
        FBallReplyResWrapDto fBallReplyResWrapDto = new FBallReplyResWrapDto();
        fBallReplyResWrapDto.setContents(fBallReply);
        fBallReplyResWrapDto.setCount(fBallReply.size());
        try {
            emitter.send(fBallReplyResWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
