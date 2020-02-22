package com.wing.forutona.FcubeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class FcubeSponsorSearch {
    int limit;
    int offset;
    String cubeuuid;
    boolean isdesc;
    String orderby;

}
