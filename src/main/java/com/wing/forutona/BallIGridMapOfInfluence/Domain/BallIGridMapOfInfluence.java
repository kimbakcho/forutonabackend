package com.wing.forutona.BallIGridMapOfInfluence.Domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"latitude","longitude"})})
@AllArgsConstructor
public class BallIGridMapOfInfluence {
    @EmbeddedId
    LatitudeLongitude latitudeLongitude;
    double MGBC;
    double MGAU;
    double MGABP;
    double HGABP;
    LocalDateTime SummaryUpdateTime;
}
