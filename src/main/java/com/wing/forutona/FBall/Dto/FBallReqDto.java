package com.wing.forutona.FBall.Dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Data
public class FBallReqDto {
    FBallType ballType;
    String ballUuid;
}
