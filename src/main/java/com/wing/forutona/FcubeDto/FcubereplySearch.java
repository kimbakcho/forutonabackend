package com.wing.forutona.FcubeDto;

public class FcubereplySearch {
    String cubeuuid;
    int offset;
    int limit;
    public FcubereplySearch(String cubeuuid,int offset,int limit){
        this.cubeuuid = cubeuuid;
        this.offset = offset;
        this.limit = limit;
    }
}
