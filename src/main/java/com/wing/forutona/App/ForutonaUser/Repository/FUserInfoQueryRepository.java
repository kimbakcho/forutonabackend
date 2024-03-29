package com.wing.forutona.App.ForutonaUser.Repository;


import com.google.firebase.auth.UserInfo;
import com.google.type.LatLng;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoSimpleResDto;
import com.wing.forutona.App.ForutonaUser.Dto.QFUserInfoResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import java.util.List;

import static com.wing.forutona.App.FBall.Domain.QFBall.fBall;
import static com.wing.forutona.App.ForutonaUser.Domain.QFUserInfo.fUserInfo;

@Repository
public class FUserInfoQueryRepository extends QuerydslRepositorySupport {

    @PersistenceContext(unitName = "forutonaApp")
    EntityManager em;

    public FUserInfoQueryRepository() {
        super(FUserInfo.class);
    }

    @PersistenceContext(unitName = "forutonaApp")
    @Override
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

    public List<FUserInfoResDto> getFindNearUserFromGeoLocationWithOutUser(LatLng latLng,
                                                                           int distance,
                                                                           FUserInfo withOutUserInfo) throws ParseException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        NumberTemplate<Integer> st_within = Expressions.numberTemplate(Integer.class, "function('st_within',{0},{1})", fUserInfo.placePoint,
                GisGeometryUtil.createDistanceEllipse(latLng, distance));

        List<FUserInfoResDto> result = queryFactory.select(new QFUserInfoResDto(fUserInfo))
                .from(fUserInfo)
                .where(st_within.eq(1))
                .where(fUserInfo.ne(withOutUserInfo))
                .fetch();

        return result;
    }

    public Page<FUserInfoSimpleResDto> findByUserNickNameWithFullTextMatchIndex(String searchNickName, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        NumberTemplate<Integer> matchTextExpressions = Expressions.numberTemplate(Integer.class,
                "function('match',{0},{1})", fUserInfo.nickName, "\'"+searchNickName+"\'");


        JPAQuery<FUserInfo> where = queryFactory.select(fUserInfo)
                .from(fUserInfo)
                .where(matchTextExpressions.eq(1));

        JPQLQuery<FUserInfo> fUserInfoJPQLQuery = getQuerydsl().applyPagination(pageable, where);
        Page<FUserInfo> page = PageableExecutionUtils.getPage(fUserInfoJPQLQuery.fetch(), pageable, fUserInfoJPQLQuery::fetchCount);


        return page.map(x -> new FUserInfoSimpleResDto(x));
    }

}
