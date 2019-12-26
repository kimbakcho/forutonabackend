package com.wing.forutona.AuthDao;

import com.wing.forutona.AuthDto.UserInfoMain;

public interface UserinfoMainMapper extends UserinfoMapper{
    int updateCurrentPosition(UserInfoMain item);
    int updateFCMtoken(UserInfoMain item);
}