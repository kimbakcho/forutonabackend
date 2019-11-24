package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.Fcubeplayer;
import com.wing.forutona.FcubeDto.FcubeplayerExtender1;

import java.util.List;

public interface FcubeplayerExtender1Mapper extends FcubeplayerMapper{
    List<FcubeplayerExtender1> selectPlayers(Fcubeplayer player);
}