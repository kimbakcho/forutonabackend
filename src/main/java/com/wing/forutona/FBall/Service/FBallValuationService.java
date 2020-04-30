package com.wing.forutona.FBall.Service;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.FBallValuationInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallValuationReqDto;
import com.wing.forutona.FBall.Dto.FBallValuationResDto;
import com.wing.forutona.FBall.Dto.FBallValuationWrapResDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FBallValuationService {

    @Autowired
    FBallValuationDataRepository fBallValuationDataRepository;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Async
    @Transactional
    public void getFBallValuation(ResponseBodyEmitter emitter, FBallValuationReqDto reqDto){
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        FUserInfo fUserInfo = new FUserInfo();
        fUserInfo.setUid(reqDto.getUid());
        List<FBallValuation> byBallUuidIsAndUidIs1 = fBallValuationDataRepository.findByBallUuidIsAndUidIs(fBall, fUserInfo);
        List<FBallValuationResDto> collect = byBallUuidIsAndUidIs1.stream().map(x -> new FBallValuationResDto(x)).collect(Collectors.toList());
        FBallValuationWrapResDto fBallValuationWrapResDto = new FBallValuationWrapResDto();
        fBallValuationWrapResDto.setCount(collect.size());
        fBallValuationWrapResDto.setContents(collect);
        try {
            emitter.send(fBallValuationWrapResDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void insertFBallValuation(ResponseBodyEmitter emitter, FBallValuationInsertReqDto reqDto) {
        try {
            FBallValuation fBallValuation = new FBallValuation();
            FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
            if(LocalDateTime.now().isAfter(fBall.getActivationTime())){
                emitter.send(-1);
                emitter.completeWithError(new Throwable("over Active Time"));
            }else {
                fBallValuation.setBallUuid(fBall);
                FUserInfo fUserInfo = new FUserInfo();
                fUserInfo.setUid(reqDto.getUid());
                fBallValuation.setUid(fUserInfo);
                fBallValuation.setUpAndDown(reqDto.getUnAndDown());
                FBallValuation save = fBallValuationDataRepository.save(fBallValuation);
                emitter.send(save.getIdx());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateFBallValuation(ResponseBodyEmitter emitter, FBallValuationInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        try {
            FBallValuation fBallValuation = fBallValuationDataRepository.findById(reqDto.getIdx()).get();
            FBall fBall = fBallValuation.getBallUuid();
            if(LocalDateTime.now().isAfter(fBall.getActivationTime())){
                emitter.send(-1);
                emitter.completeWithError(new Throwable("over Active Time"));
            }else if(fireBaseToken.getFireBaseToken().getUid().equals(fBallValuation.getUid().getUid())){
                fBallValuation.setUpAndDown(reqDto.getUnAndDown());
                emitter.send(fBallValuation.getIdx());
            }else {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("missMatch Uid"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void deleteFBallValuation(ResponseBodyEmitter emitter, Long idx, FFireBaseToken fireBaseToken) {
        try {
            FBallValuation fBallValuation = fBallValuationDataRepository.findById(idx).get();
            FBall fBall = fBallValuation.getBallUuid();
            if(LocalDateTime.now().isAfter(fBall.getActivationTime())){
                emitter.send(-1);
                emitter.completeWithError(new Throwable("over Active Time"));
            }else if(fireBaseToken.getFireBaseToken().getUid().equals(fBallValuation.getUid().getUid())){
                fBallValuationDataRepository.deleteById(idx);
                emitter.send(idx);
            }else {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("missMatch Uid"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
