package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.Fcube;

import java.util.List;

public interface FcubeExtend1Mapper extends FcubeMapper{
    List<Fcube> selectUserBoxAll(Fcube cube);
}
