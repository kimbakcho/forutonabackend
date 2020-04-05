package com.wing.forutona.CustomUtil;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageableUtil {

    public static OrderSpecifier  getDynamicOrderSpecifier(List<OrderSpecifier> orders,int index){
        if(orders.size() <= index){
            return null;
        }else {
            return orders.get(index);
        }
    }

    public static  List<OrderSpecifier> addSortOrder (Sort.Order o,EntityPathBase pathBase) {
        return  addSortOrder(null,o,pathBase);
    }

    public static  List<OrderSpecifier> addSortOrder(List<OrderSpecifier> orderBys,Sort.Order o,EntityPathBase pathBase){
        List<OrderSpecifier> tempOrderBys;
        if(orderBys == null){
            tempOrderBys = new ArrayList<>();
        }else {
            tempOrderBys = orderBys;
        }
        PathBuilder orderByExpression = new PathBuilder<>(pathBase.getType(), pathBase.getMetadata());
        OrderSpecifier orderSpecifier = new OrderSpecifier(o.isAscending() ? Order.ASC
                : Order.DESC, orderByExpression.get(o.getProperty()));
        tempOrderBys.add(orderSpecifier);
        return tempOrderBys;
    }

    public static List<OrderSpecifier> pageAbleToOrders ( Pageable pageable, EntityPathBase pathBase){
        return pageAbleToOrders(null,pageable,pathBase);
    }
    /**
     * Pageable 을 order by 에서 사용 할수 있게 만들어줌
     * @param orderBys
     * @param pageable
     * @param pathBase 적용할 Entity
     * @return
     */
    public static List<OrderSpecifier> pageAbleToOrders(List<OrderSpecifier> orderBys, Pageable pageable, EntityPathBase pathBase) {
        List<OrderSpecifier> tempOrderBys;
        if(orderBys == null){
            tempOrderBys = new ArrayList<>();
        }else {
            tempOrderBys = orderBys;
        }
        List<Sort.Order> collect = pageable.getSort().get().collect(Collectors.toList());
        for (Sort.Order order : collect) {
            PathBuilder orderByExpression = new PathBuilder<>(pathBase.getType(), pathBase.getMetadata());
            OrderSpecifier orderSpecifier = new OrderSpecifier(order.isAscending() ? Order.ASC
                    : Order.DESC, orderByExpression.get(order.getProperty()));
            tempOrderBys.add(orderSpecifier);
        }
        return tempOrderBys;
    }

    public static List<OrderSpecifier> multipleSortToOrders( List<MultiSort> sorts, EntityPathBase pathBase){
        return PageableUtil.multipleSortToOrders(null,sorts,pathBase);
    }

    public static List<OrderSpecifier> multipleSortToOrders( MultiSort sort, EntityPathBase pathBase){
        return PageableUtil.multipleSortToOrders(null,sort,pathBase);
    }

    /**
     *  Join을 하지 않는 Entity 1개로만 Multi Sort 를 할때
     * @param orderBys
     * @param sorts
     * @param pathBase 적용할 Entity
     * @return
     */
    public static List<OrderSpecifier> multipleSortToOrders(List<OrderSpecifier> orderBys, List<MultiSort> sorts, EntityPathBase pathBase) {
        List<OrderSpecifier> tempOrderBys;
        if(orderBys == null){
            tempOrderBys = new ArrayList<>();
        }else {
            tempOrderBys = orderBys;
        }
        for (MultiSort sort : sorts) {
            PathBuilder orderByExpression = new PathBuilder<>(pathBase.getType(), pathBase.getMetadata());
            OrderSpecifier orderSpecifier = new OrderSpecifier(sort.isAsc() ? Order.ASC
                    : Order.DESC, orderByExpression.get(sort.getSort()));
            tempOrderBys.add(orderSpecifier);
        }
        return tempOrderBys;
    }

    /**
     * Entity 가 Multi 일때 사용
     * @param orderBys
     * @param sort
     * @param pathBase 적용할 Entity
     * @return
     */
    public static List<OrderSpecifier> multipleSortToOrders(List<OrderSpecifier> orderBys, MultiSort sort, EntityPathBase pathBase) {
        List<OrderSpecifier> tempOrderBys;
        if(orderBys == null){
            tempOrderBys = new ArrayList<>();
        }else {
            tempOrderBys = orderBys;
        }

        PathBuilder orderByExpression = new PathBuilder<>(pathBase.getType(), pathBase.getMetadata());
        OrderSpecifier orderSpecifier = new OrderSpecifier(sort.isAsc() ? Order.ASC
                : Order.DESC, orderByExpression.get(sort.getSort()));
        tempOrderBys.add(orderSpecifier);
        return tempOrderBys;
    }
}
