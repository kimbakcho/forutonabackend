package com.wing.forutona.CustomUtil;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FSorts {

    private List<FSort> sorts;

    public List<OrderSpecifier> toOrderSpecifiers(EntityPathBase pathBase){
        return sorts.stream().map(x -> x.toOrderSpecifier(pathBase)).collect(Collectors.toList());
    }

    public boolean isContain(String str){
        for (FSort sort : sorts) {
            if(sort.getSort().equals(str)){
                return true;
            }
        }
        return false;
    }

}
