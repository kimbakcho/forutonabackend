package com.wing.forutona;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles(profiles = "local")
@Transactional
class ForutonaApplicationTests {



}
