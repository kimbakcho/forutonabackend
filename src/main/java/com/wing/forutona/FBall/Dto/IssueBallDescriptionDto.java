package com.wing.forutona.FBall.Dto;

import lombok.Data;

import java.util.List;

@Data
public class IssueBallDescriptionDto {
     String text;
     List<FBallDesImagesDto> desimages;
     String youtubeVideoId;
}
