package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FBall.Domain.FBallType;
import lombok.Data;

@Data
public class FBallUpdateReqDto {
    private String ballUuid;
    private double longitude;
    private double latitude;
    private String ballName;
    private FBallType ballType;
    private String placeAddress;
    private String description;
}
