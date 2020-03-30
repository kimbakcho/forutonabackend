package com.wing.forutona.FBall.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FBallListUpWrapDto {
    LocalDateTime searchTime;
    List<FBallResDto> balls;
}
