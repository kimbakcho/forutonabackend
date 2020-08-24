package com.wing.forutona;

import com.google.firebase.auth.FirebaseAuth;
import com.wing.forutona.CustomUtil.FAuthHttpInterceptor;
import com.wing.forutona.CustomUtil.FireBaseHandlerMethodArgumentResolver;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    protected  MockMvc mockMvc;

    @Autowired
    EntityManager em;

//    @MockBean
//    protected FAuthHttpInterceptor fAuthHttpInterceptor;
//
//    @MockBean
//    protected FireBaseHandlerMethodArgumentResolver fireBaseHandlerMethodArgumentResolver;

//    protected String testFireBaseUser = "Naver11467346";

    protected FUserInfo testUser;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    protected String customToken;

    @BeforeEach
    public void BaseSetUp() throws Exception {

//        testUser = FUserInfo.builder()
//                .uid("TEST")
//                .longitude(126.9174572)
//                .latitude(37.550045)
//                .fCMtoken("e9wJLk54v4Y:APA91bHcDrgwhsMOwUSL2DoJj-n57riAMfcCN2wHBzlniGITomzpkuLHvrSA_ggezMGlYv0AVyk4s2Z4EqFIjsadHlGsIu4eq7FUj6SFTsW06YI7HTdO-kqFsOMFYBv2PLM2QBE1lUAQ")
//                .nickName("TESTNickName")
//                .build();

        testUser = fUserInfoDataRepository.findById("7PfdwpJQqOgINy8XBWQbkqWBlqr1").get();

        customToken = FirebaseAuth.getInstance().createCustomToken(testUser.getUid());

//        given(fAuthHttpInterceptor.preHandle(any(),any(),any())).willReturn(true);
//        given(fireBaseHandlerMethodArgumentResolver.supportsParameter(any())).willReturn(false);

    }

    protected String getTestUserToken(){
        return "Bearer " + customToken;
    }


}
