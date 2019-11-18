package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.FcubeContentSelector;
import com.wing.forutona.FcubeDto.Fcubecontent;

import java.util.List;

public interface FcubecontentExtend1Mapper extends FcubecontentMapper{
    List<Fcubecontent> selectwithFcubeContentSelector(FcubeContentSelector item);
}
