package com.wing.forutona.FBall.Service.FBallType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallPlayer;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallPlayer.FBallPlayerDataRepository;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IssueBallTypeService {

    final FBallDataRepository fBallDataRepository;
    final FUserInfoDataRepository fUserInfoDataRepository;
    final GoogleStorgeAdmin googleStorgeAdmin;
    final FBallPlayerDataRepository fBallPlayerDataRepository;

    @Async
    @Transactional
    public void insertBall(ResponseBodyEmitter emitter, IssueBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        try {
            FBall fBall = FBall.builder()
                    .makeTime(LocalDateTime.now())
                    .ballState(FBallState.Play)
                    .fBallUid(FUserInfo.builder().uid(fireBaseToken.getUserFireBaseUid()).build())
                    .activationTime(LocalDateTime.now().plusDays(7))
                    //아래 지수는 액션에 의해 변해야 함으로 Client 단순 정보로 변하게 하지 않기 위해서 직접 BackEnd 에서 Defined
                    .makeExp(300)
                    .build();

            FBall saveBall = fBallDataRepository.saveAndFlush(fBall);
            FBallResDto fBallResDto = new FBallResDto(saveBall);
            emitter.send(fBallResDto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }


    @Async
    @Transactional
    public void updateBall(ResponseBodyEmitter emitter, IssueBallUpdateReqDto reqDto, FFireBaseToken fireBaseToken) {
        try {
            FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
            if (!fBall.getFBallUid().getUid().equals(fireBaseToken.getUserFireBaseUid())) {
                throw new Exception("don't Have Permisstion");
            }

            fBall.updateIssueBallUpdateReqDto(reqDto);
            List<FBalltag> tagCollect = reqDto.getTags().stream()
                    .map(x -> FBalltag.builder()
                            .ballUuid(fBall)
                            .tagItem(x.getTagItem())
                            .build()
                    ).collect(Collectors.toList());

            fBall.getTags().clear();
            fBall.getTags().addAll(tagCollect);
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void selectBall(ResponseBodyEmitter emitter, FBallReqDto fBallReqDto) {
        try {
            FBall fBall = fBallDataRepository.findById(fBallReqDto.getBallUuid()).get();
            FBallResDto fBallResDto = new FBallResDto(fBall);
            emitter.send(fBallResDto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void deleteBall(ResponseBodyEmitter emitter, FBallReqDto fBallReqDto, FFireBaseToken fireBaseToken) {
        try {
            FBall fBall = fBallDataRepository.findById(fBallReqDto.getBallUuid()).get();
            if (!fBall.getFBallUid().getUid().equals(fireBaseToken.getUserFireBaseUid())) {
                throw new Exception("don't Have Permisstion");
            }
            fBall.delete();

            ObjectMapper objectMapper = new ObjectMapper();
            IssueBallDescriptionDto issueBallDescriptionDto = objectMapper.readValue(fBall.getDescription(), IssueBallDescriptionDto.class);
            if (issueBallDescriptionDto.getDesimages() != null) {
                deleteImageFile(issueBallDescriptionDto);
            }

            fBall.setActivationTime(LocalDateTime.now());
            fBall.getTags().clear();
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void joinBall(ResponseBodyEmitter emitter, FBallJoinReqDto fBallReqDto, FFireBaseToken fireBaseToken) {
        try {
            FUserInfo fBallPlayer = FUserInfo.builder().uid(fireBaseToken.getUserFireBaseUid()).build();
            FBall fBall = FBall.builder().ballUuid(fBallReqDto.getBallUuid()).build();
            FBallPlayer ballPlayer = fBallPlayerDataRepository.findFBallPlayerByPlayerUidIsAndBallUuidIs(fBallPlayer, fBall);
            if (ballPlayer != null) {
                emitter.send(1);
            } else {
                FBallPlayer joinBallPlayer = FBallPlayer.builder()
                        .ballUuid(fBall)
                        .playerUid(fBallPlayer)
                        .startTime(LocalDateTime.now())
                        .playState(FBallPlayState.Join).build();
                fBallPlayerDataRepository.saveAndFlush(joinBallPlayer);
                emitter.send(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void ballHit(ResponseBodyEmitter emitter, FBallReqDto reqDto) {
        try {
            FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
            fBall.setBallHits(fBall.getBallHits() + 1);
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }


    public void deleteImageFile(IssueBallDescriptionDto issueBallDescriptionDto) {
        for (FBallDesImagesDto item : issueBallDescriptionDto.getDesimages()
        ) {
            int index = item.getSrc().lastIndexOf("/");
            String saveFileName = item.getSrc().substring(index);
            if (saveFileName.length() > 1) {
                BlobId blobId = BlobId.of("publicforutona", "profileimage/" + saveFileName);
                googleStorgeAdmin.GetStorageInstance().delete(blobId);
            }
        }
    }
}
