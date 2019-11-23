package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.Fcubereply;
import com.wing.forutona.FcubeDto.FcubereplyExtender1;
import com.wing.forutona.FcubeDto.FcubereplySearch;

import java.util.List;

public interface FcubereplyExtender1Mapper extends FcubereplyMapper{
    int SelectBgroubReplyMax(Fcubereply srcreply);
    int SelectStep1ForReply(Fcubereply srcreply);
    int SelectStep2ForReply(Fcubereply srcreply);
    int UpdateStep2ForReply(Fcubereply srcreply);
    int insert(Fcubereply srcreply);
    List<FcubereplyExtender1> SelectReplyForCube(FcubereplySearch searchitem);
}