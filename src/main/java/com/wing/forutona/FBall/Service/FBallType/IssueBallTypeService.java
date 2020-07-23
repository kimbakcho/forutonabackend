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


    @Transactional
    public FBallResDto insertBall(IssueBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        FBall fBall1 = FBall.fromIssueBallInsertReqDto(reqDto);
        fBall1.setUid(FUserInfo.builder().uid(fireBaseToken.getUserFireBaseUid()).build());
        FBall saveBall = fBallDataRepository.saveAndFlush(fBall1);
        FBallResDto fBallResDto = new FBallResDto(saveBall);
        return fBallResDto;
    }


    @Async
    @Transactional
    public int updateBall(IssueBallUpdateReqDto reqDto, FFireBaseToken fireBaseToken) throws Exception {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        if (!fBall.getUid().getUid().equals(fireBaseToken.getUserFireBaseUid())) {
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
        return 1;
    }

    @Transactional
    public FBallResDto selectBall(FBallReqDto fBallReqDto) {
        FBall fBall = fBallDataRepository.findById(fBallReqDto.getBallUuid()).get();
        FBallResDto fBallResDto = new FBallResDto(fBall);
        return fBallResDto;
    }

    @Transactional
    public int deleteBall(FBallReqDto fBallReqDto, FFireBaseToken fireBaseToken) throws Exception {
        FBall fBall = fBallDataRepository.findById(fBallReqDto.getBallUuid()).get();
        if (!fBall.getUid().getUid().equals(fireBaseToken.getUserFireBaseUid())) {
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
        return 1;
    }

    @Transactional
    public int joinBall(FBallJoinReqDto fBallReqDto, FFireBaseToken fireBaseToken) {
        FUserInfo fBallPlayer = FUserInfo.builder().uid(fireBaseToken.getUserFireBaseUid()).build();
        FBall fBall = FBall.builder().ballUuid(fBallReqDto.getBallUuid()).build();
        FBallPlayer ballPlayer = fBallPlayerDataRepository.findFBallPlayerByPlayerUidIsAndBallUuidIs(fBallPlayer, fBall);
        if (isNotYetJoined(ballPlayer)) {
            FBallPlayer joinBallPlayer = FBallPlayer.builder()
                    .ballUuid(fBall)
                    .playerUid(fBallPlayer)
                    .startTime(LocalDateTime.now())
                    .playState(FBallPlayState.Join).build();
            fBallPlayerDataRepository.saveAndFlush(joinBallPlayer);

        }
        return 1;

    }

    public boolean isNotYetJoined(FBallPlayer ballPlayer) {
        return ballPlayer == null;
    }

    @Transactional
    public int ballHit(FBallReqDto reqDto) {

        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        fBall.setBallHits(fBall.getBallHits() + 1);
        return 1;

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
