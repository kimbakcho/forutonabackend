package com.wing.forutona.FBall.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.CustomUtil.MultiSorts;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBall.FBallQueryRepository;
import com.wing.forutona.FBall.Domain.FMapFindScopeStep;
import com.wing.forutona.FBall.Repository.MapFindScopeStepRepository;
import com.wing.forutona.FBall.Service.BallMaker.FBallMakerFactory;
import com.wing.forutona.FBall.Service.BallMaker.FBallMakerService;
import com.wing.forutona.FBall.Service.BallMaker.IssueBallMakerService;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FTag.Dto.TagInsertReqDto;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@Service
public class FBallService {


    @Autowired
    FBallQueryRepository fBallQueryRepository;

    @Autowired
    MapFindScopeStepRepository mapFindScopeStepRepository;

    @Autowired
    GoogleStorgeAdmin googleStorgeAdmin;

    @Autowired
    FBallMakerFactory fBallMakerFactory;

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
    public void BallListUp(ResponseBodyEmitter emitter,BallFromMapAreaReqDto reqDto,
                                         MultiSorts sorts, Pageable pageable){
        try {
            emitter.send(fBallQueryRepository.getBallListUp(reqDto,sorts,pageable));
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
    public void BallListUp(ResponseBodyEmitter emitter, BallNameSearchReqDto reqDto, MultiSorts sorts, Pageable pageable) {
        try {
            emitter.send(fBallQueryRepository.getBallListUp(reqDto,sorts,pageable));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    /**
     * 중심 위치로부터 찾기
     * @param emitter
     * @param reqDto
     * @param pageable
     * @throws ParseException
     */
    @Async
    @Transactional
    public void BallListUp(ResponseBodyEmitter emitter, FBallListUpReqDto reqDto, Pageable pageable) throws ParseException {
        int findDistanceRange = this.diatanceOfBallCountToLimit(reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getBallLimit());
        try {
            emitter.send(fBallQueryRepository.getBallListUp(
                    GisGeometryUtil.createCenterPoint(reqDto.getLatitude(), reqDto.getLongitude())
                    , GisGeometryUtil.createRect(reqDto.getLatitude(), reqDto.getLongitude(), findDistanceRange)
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




    /*
    범위내 목적으로 하는 Ball갯수 까지 Rect(범위)를 확장 시켜 적합한 집계 거리 반환
     */
    @Transactional
    public int diatanceOfBallCountToLimit(double latitude, double longitude, int limit) throws ParseException {
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

    /**
     * Ball을 입력하는 부분
     * @param emitter
     * @param reqDto
     * @param fireBaseToken
     */
    @Async
    @Transactional
    public void insertBall(ResponseBodyEmitter emitter, FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        FBallMakerService fBallMakerService = fBallMakerFactory.getService(reqDto.getBallType());
        try {
            emitter.send(fBallMakerService.insertBall(reqDto,fireBaseToken));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updateBall(ResponseBodyEmitter emitter, FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        FBallMakerService fBallMakerService = fBallMakerFactory.getService(reqDto.getBallType());
        try {
            emitter.send(fBallMakerService.updateBall(reqDto,fireBaseToken));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
