package com.wing.forutona.FcubeDto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("FcubeContentSelector")
@Data
public class FcubeContentSelector {
    List<String> contenttypes;
    String cubeuuid;
    String uid;
}
