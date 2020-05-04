package com.wing.forutona.FBall.Service.BallMaker;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallInsertReqDto;
import com.wing.forutona.FBall.Dto.FBallState;
import com.wing.forutona.FBall.Dto.FBallType;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.FTag.Domain.FBalltag;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class IssueBallMakerService implements FBallMakerService{

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Override
    public int insertBall(FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        FBall fBall = new FBall(reqDto);
        fBall.setMakeTime(LocalDateTime.now());
        //이슈Ball의 경우는 Wait가 없음
        if(FBallType.IssueBall == reqDto.getBallType()){
            fBall.setBallState(FBallState.Play);
        }else {
            fBall.setBallState(FBallState.Wait);
        }
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(fireBaseToken.getFireBaseToken().getUid()).get();
        fBall.setFBallUid(fUserInfo);
        //아래 지수는 액션에 의해 변해야 함으로 Client 단순 정보로 변하게 하지 않기 위해서 직접 BackEnd 에서 Defined
        fBall.setPointReward(0);
        fBall.setInfluenceReward(0);
        fBall.setActivationTime(LocalDateTime.now().plusDays(7));
        fBall.setBallHits(0);
        fBall.setBallDisLikes(0);
        fBall.setBallPower(0);
        fBall.setJoinPlayer(0);
        fBall.setMaximumPlayers(-1);
        fBall.setStarPoints(0);
        fBall.setExpGiveFlag(0);
        fBall.setMakeExp(300);
        fBall.setCommentCount(0);
        fBall.setUserExp(0);
        FBall save = fBallDataRepository.save(fBall);
        return 1;
    }

    @Override
    public int updateBall(FBallInsertReqDto reqDto, FFireBaseToken fireBaseToken) {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        fBall.setLongitude(reqDto.getLongitude());
        fBall.setLatitude(reqDto.getLatitude());
        GeometryFactory geomFactory = new GeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(reqDto.getLongitude(), reqDto.getLatitude()));
        point.setSRID(4326);
        fBall.setPlacePoint(point);
        fBall.setBallName(reqDto.getBallName());
        fBall.setPlaceAddress(reqDto.getPlaceAddress());
        fBall.setAdministrativeArea(reqDto.getAdministrativeArea());
        fBall.setCountry(reqDto.getCountry());
        fBall.setBallPassword(reqDto.getBallPassword());
        fBall.setMaximumPlayers(reqDto.getMaximumPlayers());
        fBall.setDescription(reqDto.getDescription());
        List<FBalltag> tagCollect = reqDto.getTags().stream().map(x -> new FBalltag(fBall, x)).collect(Collectors.toList());
        fBall.setTags(tagCollect);
        return 1;
    }
}
