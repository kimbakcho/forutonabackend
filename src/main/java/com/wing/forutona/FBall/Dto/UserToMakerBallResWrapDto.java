package com.wing.forutona.FBall.Dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserToMakerBallResWrapDto {
    LocalDateTime searchTime;
    List<UserToMakerBallResDto> contents = new ArrayList<>();

    public UserToMakerBallResWrapDto(LocalDateTime searchTime, List<UserToMakerBallResDto> contents) {
        this.searchTime = searchTime;
        this.contents = contents;
    }
}
