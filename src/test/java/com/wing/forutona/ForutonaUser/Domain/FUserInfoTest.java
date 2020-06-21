package com.wing.forutona.ForutonaUser.Domain;

import com.google.type.LatLng;
import com.vividsolutions.jts.geom.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FUserInfoTest {


    FUserInfo fUserInfo;

    @BeforeEach
    void setUp(){
        fUserInfo = new FUserInfo();
    }

    @Test
    @DisplayName("PlacePoint Geo 기준 업데이트")
    void updatePlacePoint() {
        //given
        LatLng userPlacePoint = LatLng.newBuilder().setLongitude(127.0).setLatitude(31.0).build();
        //when
        fUserInfo.updatePlacePoint(userPlacePoint);
        //then
        Point placePoint = fUserInfo.getPlacePoint();
        assertEquals(31.0,placePoint.getX());
        assertEquals(127.0,placePoint.getY());
    }
}