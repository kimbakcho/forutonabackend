package com.wing.forutona.CustomUtil;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class PageableUtil {
    public static List<OrderSpecifier> pageAbleToOrders(List<OrderSpecifier> orderBys, Pageable pageable, EntityPathBase pathFBall) {
        List<Sort.Order> collect = pageable.getSort().get().collect(Collectors.toList());
        for (Sort.Order order : collect) {
            PathBuilder orderByExpression = new PathBuilder<>(pathFBall.getType(), pathFBall.getMetadata());
            OrderSpecifier orderSpecifier = new OrderSpecifier(order.isAscending() ? Order.ASC
                    : Order.DESC, orderByExpression.get(order.getProperty()));
            orderBys.add(orderSpecifier);
        }
        return orderBys;
    }
}
