package com.wing.forutona.BallIGridMapOfInfluence.Domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BallIGridMapOfInfluence {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;
    double latitude;
    double longitude;
    double MGBC;
    double MGAU;
    double MGABP;
    double HGABP;
    LocalDateTime SummaryUpdateTime;


}
