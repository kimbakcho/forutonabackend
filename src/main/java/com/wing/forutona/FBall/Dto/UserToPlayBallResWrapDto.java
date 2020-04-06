package com.wing.forutona.FBall.Dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserToPlayBallResWrapDto {
    LocalDateTime searchTime;
    List<UserToPlayBallResDto> contents = new ArrayList<>();

    public UserToPlayBallResWrapDto(LocalDateTime searchTime, List<UserToPlayBallResDto> contents) {
        this.searchTime = searchTime;
        this.contents = contents;
    }
}