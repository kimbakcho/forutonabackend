package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.FCubeGeoSearchUtil;
import com.wing.forutona.FcubeDto.Fcube;
import com.wing.forutona.FcubeDto.FcubeExtender1;

import java.util.List;

public interface FcubeExtend1Mapper extends FcubeMapper{
    List<FcubeExtender1> selectUserBoxAll(Fcube cube);

    FcubeExtender1 getFcubeExtender1(String cubeuuid);
    List<FcubeExtender1> findNearDistanceCube(FCubeGeoSearchUtil searchItem);
}
