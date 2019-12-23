package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.Fcubeplayer;
import com.wing.forutona.FcubeDto.FcubeplayerExtender1;
import com.wing.forutona.FcubeDto.FcubeplayerExtender2;
import com.wing.forutona.FcubeDto.FcubeplayerSearch;

import java.util.List;

public interface FcubeplayerExtender2Mapper extends FcubeplayerMapper{
    List<FcubeplayerExtender2> selectPlayerJoinList(FcubeplayerSearch player);

}