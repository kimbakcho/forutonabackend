package com.wing.forutona.CustomUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiSorts {

    private List<MultiSort> sorts;

    public boolean isContain(String str){
        boolean result = false;
        for (MultiSort sort : sorts) {
            if(sort.getSort().equals(str)){
                result = true;
                return true;
            }
        }
        return result;
    }

}
