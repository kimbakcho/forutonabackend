package com.wing.forutona;

import com.wing.forutona.FcubeDao.FcubeDao;
import com.wing.forutona.FcubeDto.Fcube;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ForutonaApplicationTests {

	@Autowired
	FcubeDao fcubeDao;

	@Test
	public void testcode(){
		System.out.println("test");
		Fcube cube = fcubeDao.getFcubestate("3e97416a-939d-46b8-a75f-0465fd91e759");

		System.out.println(cube.getCubestate().toString());
	}

	@Test
	void contextLoads() {

	}

}
