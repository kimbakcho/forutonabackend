package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.spatial.PointPath;
import com.querydsl.spatial.jts.JTSPointPath;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import java.awt.*;

@Data
public class FBallResDto {
    private Point placePoint;
    private double ballPower;
    private double distance;
    private String tag;
    private double influencePower;


    @QueryProjection
    public FBallResDto(Point placePoint,double distance) {
        this.placePoint = placePoint;
        this.distance = distance;
    }


}
