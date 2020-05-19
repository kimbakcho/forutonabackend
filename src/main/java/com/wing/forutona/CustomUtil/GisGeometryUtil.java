package com.wing.forutona.CustomUtil;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GisGeometryUtil {
    /*
    중심 좌표를 받아 좌표를 중심으로 정사각형 범위를 구함
   */
    public static Geometry createRect(double latitude, double longitude, double distance) throws ParseException {
        com.grum.geocalc.Coordinate lat = com.grum.geocalc.Coordinate.fromDegrees(latitude);
        com.grum.geocalc.Coordinate lng = com.grum.geocalc.Coordinate.fromDegrees(longitude);
        com.grum.geocalc.Point findPosition = com.grum.geocalc.Point.at(lat, lng);
        BoundingArea area = EarthCalc.around(findPosition, distance / 2);
        Geometry rect = new WKTReader().read(createRectPOLYGONStr(area.southWest.longitude, area.southWest.latitude, area.northEast.longitude, area.northEast.latitude));
        rect.setSRID(4326);
        return rect;
    }


    /*
    select ST_ASText(ST_Envelope(ST_GeomFromText("LineString(127.2854264529815 37.07021091269403, 127.675837138646 38.70836583519889)",4326)))
    위와 같은 함수를 대신함
    https://dev.mysql.com/doc/refman/8.0/en/gis-general-property-functions.html
     */
    public static String createRectPOLYGONStr(double southWestlongitude, double southWestlatitude, double northEastlongitude, double northEastlatitude) {
        String src = "POLYGON((" + southWestlongitude + " " + southWestlatitude + "," +
                northEastlongitude + " " + southWestlatitude + "," +
                northEastlongitude + " " + northEastlatitude + "," +
                southWestlongitude + " " + northEastlatitude + "," +
                southWestlongitude + " " + southWestlatitude + "))";
        return src;
    }

    public static Geometry createCenterPoint(double latitude, double longitude) throws ParseException {
        Geometry circlePoint = new WKTReader().read("Point(" + longitude + " " + latitude + ")");
        circlePoint.setSRID(4326);
        return circlePoint;
    }

}
