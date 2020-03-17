package com.wing.forutona.FcubeDto;

import lombok.Data;

import java.util.Date;

@Data
public class FCubeGeoSearchUtil extends GeoSearchUtil{
    int cubestate;
    Date activationtime;
    int cubescope;
    double southWestlatitude;
    double southWestlongitude;
    double northEastlatitude;
    double northEastlongitude;
    int findlimitcout;

}
