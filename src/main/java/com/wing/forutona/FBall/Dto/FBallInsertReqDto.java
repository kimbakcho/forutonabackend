package com.wing.forutona.FBall.Dto;

import com.wing.forutona.FTag.Dto.TagInsertReqDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 해당 Dto가 기본 Fall Insert Dto
 */
@Data
public class FBallInsertReqDto {
    private String ballUuid;
    private double longitude;
    private double latitude;
    private String ballName;
    private FBallType ballType;
    private String placeAddress;
    private String administrativeArea;
    private String country;
    private String ballPassword;
    private long maximumPlayers;
    private String description;
    List<TagInsertReqDto> tags;
}
