package com.wing.forutona.CustomUtil;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FSort {
    @JsonProperty("order")
    private Order order;
    @JsonProperty("sort")
    private String sort;

    public FSort(String sort, Order order) {
        this.sort = sort;
        this.order = order;
    }
    public OrderSpecifier toOrderSpecifier (EntityPathBase pathBase){
        PathBuilder orderByExpression = new PathBuilder<>(pathBase.getType(), pathBase.getMetadata());
        OrderSpecifier orderSpecifier = new OrderSpecifier(isAsc() ? Order.ASC
                : Order.DESC, orderByExpression.get(sort));
        return orderSpecifier;
    }

    boolean isAsc(){
        return this.order.equals(Order.ASC);
    }


    @Override
    public boolean equals(Object obj) {
        String test = (String) obj;
        return test.equals(sort);
    }

}
