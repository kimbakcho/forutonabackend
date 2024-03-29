package com.wing.forutona.App.FBall.Service.BallInsert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.FBallState;
import com.wing.forutona.App.FBall.Dto.FBallInsertReqDto;
import com.wing.forutona.App.FBall.Dto.FBallResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FTag.Domain.FBalltag;
import com.wing.forutona.App.FTag.Repository.FBallTagDataRepository;
import com.wing.forutona.App.FireBaseMessage.Service.FBallInsertFCMService;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("BallInsertServiceImpl")
@Transactional
@RequiredArgsConstructor
class BallInsertServiceImpl implements BallInsertService {

    final FBallDataRepository fBallDataRepository;

    final FBallInsertFCMService fBallInsertFCMService;

    final FUserInfoDataRepository fUserInfoDataRepository;

    final FBallTagDataRepository fBallTagDataRepository;


    @Override
    public FBallResDto insertBall(FBallInsertReqDto reqDto, String userUid) throws ParseException {

        GeometryFactory geomFactory = new GeometryFactory();
        Point placePoint = geomFactory.createPoint(new Coordinate(reqDto.getLongitude(), reqDto.getLatitude()));
        placePoint.setSRID(4326);

        Optional<FUserInfo> userInfo = fUserInfoDataRepository.findById(userUid);

        FBall fBall = FBall.builder().ballUuid(reqDto.getBallUuid())
                .makeTime(LocalDateTime.now())
                .ballState(FBallState.Play)
                .longitude(reqDto.getLongitude())
                .latitude(reqDto.getLatitude())
                .activationTime(LocalDateTime.now().plusDays(7))
                .ballName(reqDto.getBallName())
                .ballType(reqDto.getBallType())
                .placeAddress(reqDto.getPlaceAddress())
                .description(reqDto.getDescription())
                .ballPower(0)
                .ballHits(0)
                .makeExp(300)
                .uid(userInfo.get())
                .influenceReward(0L)
                .pointReward(0L)
                .build();

        FBall saveBall = fBallDataRepository.saveAndFlush(fBall);

        for(int i=0;i<reqDto.getTags().size();i++){
            FBalltag tag = FBalltag.builder().ballUuid(saveBall)
                    .tagItem(reqDto.getTags().get(i).getTagItem())
                    .tagIndex(i)
                    .build();
            fBallTagDataRepository.save(tag);
        }

        fBallInsertFCMService.sendInsertFCMMessage(saveBall);

        return new FBallResDto(saveBall);
    }
}
