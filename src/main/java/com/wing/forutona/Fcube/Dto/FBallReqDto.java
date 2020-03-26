package com.wing.forutona.Fcube.Dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Data
public class FBallReqDto {
    String uid;
    String ballUuid;
    Double longitude;
    Double latitude;
    String matchBallName;
    FBallType fBallType;
    LocalDateTime makeTime;
    FBallState fBallState;
    Pageable page;
}
