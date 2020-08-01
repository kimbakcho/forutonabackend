package com.wing.forutona.FBall.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.FireBaseMessage.Service.FBallInsertFCMService;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BallInsertService {
    FBallResDto insertBall(FBallInsertReqDto reqDto, String userUid) throws ParseException;
}
@Service
@Transactional
@RequiredArgsConstructor
class BallInsertServiceImpl implements BallInsertService {

    final FBallDataRepository fBallDataRepository;

    final FBallInsertFCMService fBallInsertFCMService;

    final FUserInfoDataRepository fUserInfoDataRepository;


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
                .placePoint(placePoint)
                .ballName(reqDto.getBallName())
                .ballType(reqDto.getBallType())
                .placeAddress(reqDto.getPlaceAddress())
                .description(reqDto.getDescription())
                .makeExp(300)
                .uid(userInfo.get())
                .build();

        List<FBalltag> tagCollect = reqDto.getTags().stream()
                .map(x -> FBalltag.builder()
                        .ballUuid(fBall)
                        .tagItem(x.getTagItem())
                        .build()
                ).collect(Collectors.toList());

        fBall.setTags(tagCollect);


        FBall saveBall = fBallDataRepository.saveAndFlush(fBall);

        fBallInsertFCMService.sendInsertFCMMessage(saveBall);
        return new FBallResDto(saveBall);
    }
}
