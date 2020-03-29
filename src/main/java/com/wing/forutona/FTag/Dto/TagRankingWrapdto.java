package com.wing.forutona.FTag.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRankingWrapdto {
    LocalDateTime searchTime;
    List<TagRankingDto> contents;
}
