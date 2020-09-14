package com.wing.forutona;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.geom.*;

import com.vividsolutions.jts.util.GeometricShapeFactory;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import com.wing.forutona.FBall.Repository.FBallDataRepository;
import com.wing.forutona.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.FBall.Service.DistanceOfBallCountToLimitService;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Repository.FBallTagQueryRepository;
import com.wing.forutona.FTag.Service.FTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@ActiveProfiles(profiles = "local")
@Transactional
class ForutonaApplicationTests {



}
