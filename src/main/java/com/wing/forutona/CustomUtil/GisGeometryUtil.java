package com.wing.forutona.CustomUtil;

import com.google.type.LatLng;
import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.EarthCalc;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import java.util.Random;

public class GisGeometryUtil {
    /*
    중심 좌표를 받아 좌표를 중심으로 정사각형 범위를 구함
   */
    public static Geometry createSquareFromCenterPosition(LatLng centerPosition, double distance) throws ParseException {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setCentre(new Coordinate(centerPosition.getLongitude(), centerPosition.getLatitude()));
        shapeFactory.setHeight(distance*0.0000092);
        shapeFactory.setWidth(distance*0.0000115);
        Polygon boundArea = shapeFactory.createRectangle();
        boundArea.setSRID(4326);
        return boundArea;
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

    public static Geometry createPoint(double latitude, double longitude) throws ParseException {
        Geometry circlePoint = new WKTReader().read("Point(" + longitude + " " + latitude + ")");
        circlePoint.setSRID(4326);
        return circlePoint;
    }
    public static double getDistance(LatLng sourcePosition,LatLng targetPosition){
        GeometryFactory geomFactory = new GeometryFactory();
        Point sourcePoint = geomFactory.createPoint(new Coordinate(sourcePosition.getLongitude(), sourcePosition.getLatitude()));
        sourcePoint.setSRID(4326);
        Point targetPoint = geomFactory.createPoint(new Coordinate(targetPosition.getLongitude(), targetPosition.getLatitude()));
        targetPoint.setSRID(4326);
        return sourcePoint.distance(targetPoint);
    }

    public static double getDistance(Geometry sourcePosition,LatLng targetPosition){
        GeometryFactory geomFactory = new GeometryFactory();
        Point targetPoint = geomFactory.createPoint(new Coordinate(targetPosition.getLongitude(), targetPosition.getLatitude()));
        targetPoint.setSRID(4326);
        return sourcePosition.distance(targetPoint) * 100000;
    }

    public static Geometry createDistanceEllipse(LatLng centerPosition,int distance){
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(120);
        shapeFactory.setCentre(new Coordinate(centerPosition.getLongitude(), centerPosition.getLatitude()));
        shapeFactory.setHeight(2* (distance*0.0000092));
        shapeFactory.setWidth(2* (distance*0.0000115));
        Polygon circle = shapeFactory.createEllipse();
        circle.setSRID(4326);
        return circle;
    }


    public static LatLng getRandomLocation(double longitude, double latitude, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        return getLatLng(longitude, latitude, radiusInDegrees, u, v);
    }
    public static LatLng getRandomExactLocation(double longitude, double latitude, int radius) {
        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;
        double u = 1;
        double v = 1;
        return getLatLng(longitude, latitude, radiusInDegrees, u, v);
    }

    public static LatLng getLatLng(double longitude, double latitude, double radiusInDegrees, double u, double v) {
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(latitude));

        double foundLongitude = new_x + longitude;
        double foundLatitude = y + latitude;
        LatLng result = LatLng.newBuilder().setLongitude(foundLongitude).setLatitude(foundLatitude).build();
        return result;
    }
}
