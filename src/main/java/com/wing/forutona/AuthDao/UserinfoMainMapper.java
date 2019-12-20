package com.wing.forutona.AuthDao;

import com.wing.forutona.AuthDto.UserInfoMain;
import com.wing.forutona.AuthDto.Userinfo;

import java.util.List;

public interface UserinfoMainMapper extends UserinfoMapper{
    int updateCurrentPosition(UserInfoMain item);
    int updateFCMtoken(UserInfoMain item);
}