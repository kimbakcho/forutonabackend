package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.Fcubequestsuccess;
import com.wing.forutona.FcubeDto.FcubequestsuccessExtender1;

import java.util.List;

public interface FcubequestsuccessExtender1Mapper extends  FcubequestsuccessMapper{
    List<FcubequestsuccessExtender1> getQuestReqList(FcubequestsuccessExtender1 item);
    List<FcubequestsuccessExtender1> getQuestSucessList(FcubequestsuccessExtender1 item);
    int updateAllReadingCheck(FcubequestsuccessExtender1 item);
    int updateQuestReq(FcubequestsuccessExtender1 item);
    int updateQuesttoplayercomment(FcubequestsuccessExtender1 item);
    List<FcubequestsuccessExtender1> getPlayerQuestSuccessList(FcubequestsuccessExtender1 item);

}