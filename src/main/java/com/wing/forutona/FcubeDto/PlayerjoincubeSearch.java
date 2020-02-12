package com.wing.forutona.FcubeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PlayerjoincubeSearch {
    String playerUid;
    String orderBy = "ActivationTime";
    double PlayerLatitude;
    double PlayerLongitude;
    boolean isDesc = true;
    int limit = 10;
    int offset = 0;
}
