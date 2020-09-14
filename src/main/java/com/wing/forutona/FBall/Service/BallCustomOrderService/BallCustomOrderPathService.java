package com.wing.forutona.FBall.Service.BallCustomOrderService;

import com.google.type.LatLng;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.CustomUtil.GisGeometryUtil;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.QFBall;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.wing.forutona.FBall.Domain.QFBall.fBall;

public interface BallCustomOrderPathService {
    List<OrderSpecifier> make() throws ParseException;
}

class BallCustomOrderDistance implements BallCustomOrderPathService {
    static String orderKey = "distance";
    LatLng position;
    Sort.Order sort;

    public BallCustomOrderDistance(LatLng position, Sort.Order sort) {
        this.position = position;
        this.sort = sort;
    }

    @Override
    public List<OrderSpecifier> make() throws ParseException {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (sort.getProperty().equals(orderKey)) {
            NumberTemplate st_distance_sphere = Expressions.numberTemplate(Double.class,
                    "function('st_distance_sphere',{0},{1})", fBall.placePoint,
                    GisGeometryUtil.createPoint(position.getLatitude(), position.getLongitude()));
            orderSpecifiers.add(st_distance_sphere.desc());
        }
        return orderSpecifiers;
    }
}

class BallCustomOrderMakeTimeDESCAliveDESC implements BallCustomOrderPathService {
    static String orderKey = "makeTimeDESCAliveDESC";
    Sort.Order sort;

    public BallCustomOrderMakeTimeDESCAliveDESC(Sort.Order sort) {
        this.sort = sort;
    }

    @Override
    public List<OrderSpecifier> make() throws ParseException {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if (sort.getProperty().equals(orderKey)) {
            NumberExpression<Integer> Alive = new CaseBuilder()
                    .when(fBall.activationTime.after(LocalDateTime.now()))
                    .then(1)
                    .otherwise(0);
            orderSpecifiers.add(fBall.makeTime.desc());
            orderSpecifiers.add(Alive.desc());
        }
        return orderSpecifiers;
    }
}

class BallNoCustomOrder implements BallCustomOrderPathService {
    Sort.Order sort;

    BallNoCustomOrder(Sort.Order sort) {
        this.sort = sort;
    }

    @Override
    public List<OrderSpecifier> make() throws ParseException {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        PathBuilder<QFBall> entityPath = new PathBuilder(FBall.class, "fBall");
        PathBuilder<Object> path = entityPath.get(sort.getProperty());
        orderSpecifiers.add(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(sort.getDirection().name()), path));
        return orderSpecifiers;
    }
}