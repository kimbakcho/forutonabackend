package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FTag.Dto.TagInsertReqDto;
import lombok.Data;

import java.util.List;

@Data
public class FBallUpdateReqDto {
    private String ballUuid;
    private double longitude;
    private double latitude;
    private String ballName;
    private FBallType ballType;
    private String placeAddress;
    private String description;
    List<TagInsertReqDto> tags;
}
