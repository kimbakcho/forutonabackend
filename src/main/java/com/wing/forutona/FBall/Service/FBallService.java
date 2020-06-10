package com.wing.forutona.FBall.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.FBall.Repository.MapFindScopeStepRepository;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FBallService {

    final private FBallQueryRepository fBallQueryRepository;
    final private FBallDataRepository fBallDataRepository;
    final private MapFindScopeStepRepository mapFindScopeStepRepository;
    final private GoogleStorgeAdmin googleStorgeAdmin;

    @Async
    @Transactional
    public void ballImageUpload(ResponseBodyEmitter emitter,List<MultipartFile> files){
        Storage storage = googleStorgeAdmin.GetStorageInstance();
        FBallImageUploadResDto fBallImageUploadResDto = new FBallImageUploadResDto();
        fBallImageUploadResDto.setCount(files.size());
        List<String> urls = fBallImageUploadResDto.getUrls();
        try {
            for (MultipartFile file : files) {
                String OriginalFile = file.getOriginalFilename();
                int extentIndex = OriginalFile.lastIndexOf(".");
                UUID uuid = UUID.randomUUID();
                String saveFileName = "";
                if (extentIndex > 0) {
                    String extent = OriginalFile.substring(extentIndex);
                    saveFileName = uuid.toString() + extent;
                } else {
                    saveFileName = uuid.toString();
                }
                BlobId blobId = BlobId.of("publicforutona", "profileimage/"+saveFileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                storage.create(blobInfo, file.getBytes());
                String imageUrl = "https://storage.googleapis.com/publicforutona/profileimage/"+saveFileName;
                urls.add(imageUrl);
            }
            emitter.send(fBallImageUploadResDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    /**
     * 특정 범위안에 볼 찾기
     * @param emitter
     * @param reqDto
     * @param sorts
     * @param pageable
     */
    @Async
    @Transactional
    public void BallListUpFromMapArea(ResponseBodyEmitter emitter, BallFromMapAreaReqDto reqDto,
                                      MultiSorts sorts, Pageable pageable){
        try {
            emitter.send(fBallQueryRepository.getBallListUpFromMapArea(reqDto,sorts,pageable));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    /**
     * 볼이름으로 찾기
     * @param emitter
     * @param reqDto
     * @param sorts
     * @param pageable
     */
    @Async
    @Transactional
    public void BallListUpFromSearchTitle(ResponseBodyEmitter emitter, FBallListUpFromSearchTitleReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        try {
            emitter.send(fBallQueryRepository.getBallListUpFromSearchTitle(reqDto,sorts,pageable));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void BallListUpFromBallInfluencePower(ResponseBodyEmitter emitter, FBallListUpFromBallInfluencePowerReqDto reqDto, Pageable pageable) throws ParseException {
        int findDistanceRangeLimit = this.distanceOfBallCountToLimit(reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getBallLimit());
        try {
            emitter.send(fBallQueryRepository.getBallListUpFromBallInfluencePower(
                    GisGeometryUtil.createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude())
                    , GisGeometryUtil.createRect(reqDto.getLatitude(), reqDto.getLongitude(), findDistanceRangeLimit)
                    , pageable));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void getUserToMakerBalls(ResponseBodyEmitter emitter, UserToMakerBallReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        try {
            List<UserToMakerBallResDto> userToMakerBalls = fBallQueryRepository.getUserToMakerBalls(reqDto, sorts, pageable);
            UserToMakerBallResWrapDto resWrapDto = new UserToMakerBallResWrapDto(LocalDateTime.now(), userToMakerBalls);
            emitter.send(resWrapDto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emitter.complete();
        }
    }



    @Transactional
    public int distanceOfBallCountToLimit(double latitude, double longitude, int limit) throws ParseException {
        List<FMapFindScopeStep> scopeMeter = mapFindScopeStepRepository.findAll(Sort.by("scopeMeter").ascending());
        int currentScopeMater = 0;
        for (FMapFindScopeStep mapFindScopeStep : scopeMeter) {
            currentScopeMater = mapFindScopeStep.getScopeMeter();
            Geometry rect = GisGeometryUtil.createRect(latitude, longitude, currentScopeMater);
            if (fBallQueryRepository.getFindBallCountInDistance(rect) > limit) {
                return currentScopeMater;
            }
        }
        return currentScopeMater;
    }

    @Async
    @Transactional
    public void getUserToMakerBall(ResponseBodyEmitter emitter, UserToMakerBallSelectReqDto reqDto) {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        UserToMakerBallResDto userToMakerBallResDto= new UserToMakerBallResDto(fBall);
        try {
            emitter.send(userToMakerBallResDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void ListUpFromTagName(ResponseBodyEmitter emitter, FBallListUpFromTagReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        try {
            FBallListUpWrapDto fBallListUpWrapDto = fBallQueryRepository.ListUpFromTagName(reqDto,sorts,pageable);
            emitter.send(fBallListUpWrapDto);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Transactional
    public void increaseContributor(String ballUuid, Long point){
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        fBall.setContributor(fBall.getContributor() + point);
        fBallDataRepository.flush();;
    }

    @Transactional
    public void decreaseContributor(String ballUuid, Long point) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        fBall.setContributor(fBall.getContributor() - point);
        fBallDataRepository.flush();;
    }


}
