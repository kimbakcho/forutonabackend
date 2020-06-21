package com.wing.forutona.FBall.Service;

import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallValuation;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallValuation.FBallValuationDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FBallValuationService {

    final private FBallValuationDataRepository fBallValuationDataRepository;
    final private FBallDataRepository fBallDataRepository;
    final private FUserInfoDataRepository fUserInfoDataRepository;
    final private ContributorsControllerService contributorsControllerService;


    @Async
    @Transactional
    public void getFBallValuation(ResponseBodyEmitter emitter, FBallValuationReqDto reqDto) {
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
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void insertFBallValuation(ResponseBodyEmitter emitter, FBallValuationInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        try {
            FBallValuation fBallValuation = new FBallValuation();
            FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
            if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("over Active Time"));
            } else {
                fBallValuation.setValueUuid(reqDto.getValueUuid());
                fBallValuation.setBallUuid(fBall);
                FUserInfo playerUid = new FUserInfo();
                playerUid.setUid(fireBaseToken.getUserFireBaseUid());
                fBallValuation.setUid(playerUid);
                ballValuation(reqDto, fBallValuation, fBall);
                contributorsControllerService.ifNotExistsInsert(new ContributorReqDto(reqDto.getUid(),reqDto.getBallUuid()));

                FBallValuation save = fBallValuationDataRepository.save(fBallValuation);
                FBallValuationResDto fBallValuationResDto = new FBallValuationResDto(save);
                emitter.send(fBallValuationResDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateFBallValuation(ResponseBodyEmitter emitter, FBallValuationInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        try {
            FBallValuation fBallValuation = fBallValuationDataRepository.findById(reqDto.getValueUuid()).get();
            FBall fBall = fBallValuation.getBallUuid();
            if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("over Active Time"));
            } else if (fireBaseToken.getUserFireBaseUid().equals(fBallValuation.getUid().getUid())) {
                ballValuation(reqDto, fBallValuation, fBall);
                emitter.send(fBallValuation.getValueUuid());
            } else {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("missMatch Uid"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    private void ballValuation(FBallValuationInsertReqDto reqDto, FBallValuation fBallValuation, FBall fBall) {
        fBallValuation.setUpAndDown(reqDto.getUpAndDown());
        if (reqDto.getUpAndDown() > 0) {
            fBall.setBallLikes(fBall.getBallLikes() + Math.abs(reqDto.getUpAndDown()));
        } else {
            fBall.setBallDisLikes(fBall.getBallDisLikes() + Math.abs(reqDto.getUpAndDown()));
        }
        fBall.setBallPower(fBall.getBallLikes() - fBall.getBallDisLikes());
        FUserInfo makerUid = fUserInfoDataRepository.findById(fBall.getFBallUid().getUid()).get();
        makerUid.setCumulativeInfluence(makerUid.getCumulativeInfluence() + reqDto.getUpAndDown());
    }

    @Async
    @Transactional
    public void deleteFBallValuation(ResponseBodyEmitter emitter, String valueUuid, FFireBaseToken fireBaseToken) {
        try {
            FBallValuation fBallValuation = fBallValuationDataRepository.findById(valueUuid).get();
            FBall fBall = fBallValuation.getBallUuid();
            if (LocalDateTime.now().isAfter(fBall.getActivationTime())) {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("over Active Time"));
            } else if (fireBaseToken.getUserFireBaseUid().equals(fBallValuation.getUid().getUid())) {
                if (fBallValuation.getUpAndDown() > 0) {
                    fBall.setBallLikes(fBall.getBallLikes() - Math.abs(fBallValuation.getUpAndDown()));
                } else {
                    fBall.setBallDisLikes(fBall.getBallDisLikes() - Math.abs(fBallValuation.getUpAndDown()));
                }
                fBall.setBallPower(fBall.getBallPower() + fBallValuation.getUpAndDown());
                FUserInfo makerUid = fUserInfoDataRepository.findById(fBall.getFBallUid().getUid()).get();
                makerUid.setCumulativeInfluence(makerUid.getCumulativeInfluence() - fBallValuation.getUpAndDown());
                fBallValuationDataRepository.deleteById(valueUuid);
                contributorsControllerService.deleteContributorsByUidIsAndBallUuidIs(new ContributorReqDto(makerUid.getUid(),fBall.getBallUuid()));
                emitter.send(valueUuid);
            } else {
                emitter.send(-1);
                emitter.completeWithError(new Throwable("missMatch Uid"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }
}
