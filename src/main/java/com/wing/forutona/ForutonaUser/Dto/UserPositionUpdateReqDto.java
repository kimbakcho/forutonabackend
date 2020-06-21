package com.wing.forutona.ForutonaUser.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class UserPositionUpdateReqDto {
    double lat;
    double lng;

    public UserPositionUpdateReqDto(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
