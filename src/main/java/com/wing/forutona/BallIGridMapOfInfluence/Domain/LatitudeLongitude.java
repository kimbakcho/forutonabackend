package com.wing.forutona.BallIGridMapOfInfluence.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Embeddable
@Getter
@NoArgsConstructor
public class LatitudeLongitude implements Serializable {

    public LatitudeLongitude(double latitude,double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    double latitude;
    double longitude;


}
