package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.FcubeContentSelector;
import com.wing.forutona.FcubeDto.Fcubecontent;
import com.wing.forutona.FcubeDto.FcubeplayercontentExtender1;
import com.wing.forutona.FcubeDto.FcubeplayercontentSelector;

import java.util.List;

public interface FcubeplayercontentExtender1Mapper extends FcubeplayercontentMapper{
    List<FcubeplayercontentExtender1> selectwithFcubeplayercontentSelector(FcubeplayercontentSelector item);
    int updateFcubeplayercontent(FcubeplayercontentExtender1 item);
    int deleteFcubeplayercontent(FcubeplayercontentExtender1 item);
}
