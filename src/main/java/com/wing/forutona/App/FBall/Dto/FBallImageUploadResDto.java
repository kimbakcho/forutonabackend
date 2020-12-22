package com.wing.forutona.App.FBall.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class FBallImageUploadResDto {
    int count;
    List<String> urls = new ArrayList<>();
}
