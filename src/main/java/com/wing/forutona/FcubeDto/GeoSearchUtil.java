package com.wing.forutona.FcubeDto;

import lombok.Data;

@Data
public class GeoSearchUtil {
    //범위내 검색(Rnage) Miter
    double distance;
    //해당 위경도 중심을 검색
    double latitude;
    double longitude;
    int offset;
    int limit;
}
