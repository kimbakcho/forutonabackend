package com.wing.forutona.CustomUtil;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.Order;
import lombok.*;
import org.aspectj.weaver.ast.Or;

@Getter
@Setter
@NoArgsConstructor
public class MultiSort{
    @JsonProperty("order")
    private Order order;
    @JsonProperty("sort")
    private String sort;

    public MultiSort(String sort, Order order) {
        this.sort = sort;
        this.order = order;
    }
    boolean isAsc(){
        return this.order.equals("ASC");
    }


    @Override
    public boolean equals(Object obj) {
        String test = (String) obj;
        return test.equals(sort);
    }

}
