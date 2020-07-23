package com.wing.forutona;

import com.wing.forutona.CustomUtil.FAuthHttpInterceptor;
import com.wing.forutona.CustomUtil.FireBaseHandlerMethodArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    protected  MockMvc mockMvc;

    @MockBean
    protected FAuthHttpInterceptor fAuthHttpInterceptor;

    @MockBean
    protected FireBaseHandlerMethodArgumentResolver fireBaseHandlerMethodArgumentResolver;

    protected String testFireBaseUser = "Naver11467346";

    @BeforeEach
    public void BaseSetUp() throws Exception {
        given(fAuthHttpInterceptor.preHandle(any(),any(),any())).willReturn(true);
        given(fireBaseHandlerMethodArgumentResolver.supportsParameter(any())).willReturn(false);
    }

}
