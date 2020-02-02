package com.wing.forutona.FcubeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FcubeSearch {
    String uid;
    int limit;
    int offset;
    String orderby;
    boolean isdesc;
}
