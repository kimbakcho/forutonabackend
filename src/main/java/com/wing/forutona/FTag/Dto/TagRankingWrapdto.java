package com.wing.forutona.FTag.Dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagRankingWrapdto {
    LocalDateTime searchTime;
    List<TagRankingDto> contents;
}
