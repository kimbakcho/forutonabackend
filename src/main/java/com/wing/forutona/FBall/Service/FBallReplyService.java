package com.wing.forutona.FBall.Service;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.*;
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
import java.util.stream.Collectors;

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
        fBallReply.setReplyUuid(reqDto.getReplyUuid());
        fBallReply.setDeleteFlag(false);
        fBallReply.setReplyBallUuid(fBall);
        //대댓글이 아닌 처음 댓글
        if (reqDto.getReplyNumber() == -1) {
            FBallReply top1ByReplyBallUuidIsOrderByReplyNumberDesc = fBallReplyDataRepository.findTop1ByReplyBallUuidIsOrderByReplyNumberDesc(fBall);
            fBallReply.setReplyNumber(0L);
            if (top1ByReplyBallUuidIsOrderByReplyNumberDesc == null) {
                fBallReply.setReplyNumber(0L);
            } else {
                fBallReply.setReplyNumber(top1ByReplyBallUuidIsOrderByReplyNumberDesc.getReplyNumber() + 1);
            }
            fBallReply.setReplySort(0L);
            fBallReply.setReplyDepth(0L);
        } else {
            if(reqDto.getReplySort() < 1){
                reqDto.setReplySort(1L);
            }
            fBallReplyQueryRepository.updateReplySortPlusOne(fBall, reqDto.getReplyNumber(), reqDto.getReplySort());
            fBallReply.setReplySort(reqDto.getReplySort());
            fBallReply.setReplyDepth(1L);
            fBallReply.setReplyNumber(reqDto.getReplyNumber());
        }
        FUserInfo fUserInfo1 = fUserInfoDataRepository.findById(fireBaseToken.getFireBaseToken().getUid()).get();
        fBallReply.setReplyUid(fUserInfo1);
        fBallReply.setReplyText(reqDto.getReplyText());
        fBallReply.setReplyUploadDateTime(LocalDateTime.now());
        fBallReply.setReplyUpdateDateTime(fBallReply.getReplyUploadDateTime());
        fBallReplyDataRepository.saveAndFlush(fBallReply);
        FBallReplyResDto fBallReplyResDto =  new FBallReplyResDto(fBallReply);

        try {
            emitter.send(fBallReplyResDto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    /**
     * 조회 할때 처음 대표 댓글만 가져오는 부분.
     * replyDepth 을 0으로 해서 가져옴 대표 댓글의 댓글은 가져오지 않음.
     *
     * @param emitter
     * @param reqDto
     * @param pageable
     */
    @Async
    @Transactional
    public void getFBallReply(ResponseBodyEmitter emitter, FBallReplyReqDto reqDto, Pageable pageable) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        FBallReplyResWrapDto fBallReply = fBallReplyQueryRepository.getFBallReply(fBall, pageable);
        try {
            emitter.send(fBallReply);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getFBallDetailReply(ResponseBodyEmitter emitter, FBallReplyReqDto reqDto, Pageable pageable) {
        FBallReplyResWrapDto fBallDetailReply = fBallReplyQueryRepository.getFBallDetailReply(reqDto, pageable);
        try {
            emitter.send(fBallDetailReply);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateFBallReply(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken, FBallReplyInsertReqDto reqDto) {
        FBallReply fBallReply = fBallReplyDataRepository.findById(reqDto.getReplyUuid()).get();
        try {
            if (fireBaseToken.getFireBaseToken().getUid().equals(fBallReply.getReplyUid().getUid())) {
                fBallReply.setReplyText(reqDto.getReplyText());
                fBallReply.setReplyUpdateDateTime(LocalDateTime.now());
                emitter.send(1);
            } else {
                emitter.send(0);
                emitter.completeWithError(new Throwable("missMatchUid"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void deleteFBallReply(ResponseBodyEmitter emitter, FFireBaseToken fireBaseToken, String replyUuid) {
        FBallReply fBallReply = fBallReplyDataRepository.findById(replyUuid).get();
        try {
            if (fireBaseToken.getFireBaseToken().getUid().equals(fBallReply.getReplyUid().getUid())) {
                fBallReply.setReplyText("Delete Text");
                fBallReply.setReplyUpdateDateTime(LocalDateTime.now());
                fBallReply.setDeleteFlag(true);
                emitter.send(1);
            } else {
                emitter.send(0);
                emitter.completeWithError(new Throwable("missMatchUid"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getFBallSubReply(ResponseBodyEmitter emitter, FBallReplyReqDto reqDto) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        List<FBallReply> subReplyItem = fBallReplyDataRepository.
                findByReplyBallUuidIsAndReplyNumberIsOrderByReplyUploadDateTimeDesc(fBall, reqDto.getReplyNumber());

        List<FBallReplyResDto> collect = subReplyItem.stream().map(x -> new FBallReplyResDto(x)).collect(Collectors.toList());
        //제일 밑에는 시간순 정렬이기 때문에 Sort 0이 존재함 그래서 부모 댓글은 지워주고  리턴
        collect.remove(collect.size()-1);
        FBallReplyResWrapDto fBallReplyResWrapDto = new FBallReplyResWrapDto();
        fBallReplyResWrapDto.setCount(collect.size());
        fBallReplyResWrapDto.setContents(collect);
        try {
            emitter.send(fBallReplyResWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
