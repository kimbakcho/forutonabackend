package com.wing.forutona;

import com.grum.geocalc.BoundingArea;
import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import com.wing.forutona.FcubeDao.FcubeDao;
import com.wing.forutona.FcubeDto.FCubeGeoSearchUtil;
import com.wing.forutona.FcubeDto.Fcube;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ForutonaApplicationTests {

	@Autowired
	FcubeDao fcubeDao;


	@Resource(name = "sqlSession")
	private SqlSession sqlSession;

	@Test
	public void testcode(){


	}

	@Test
	void contextLoads() {

	}

}
