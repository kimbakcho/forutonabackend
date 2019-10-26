package com.wing.forutona.AuthDao;

import com.wing.forutona.AuthDto.Userinfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserInfoDao {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    public int InsertUserInfo(Userinfo param) {
        UserinfoMapper mapper = sqlSession.getMapper(UserinfoMapper.class);

        return mapper.insert(param);
    }


}
