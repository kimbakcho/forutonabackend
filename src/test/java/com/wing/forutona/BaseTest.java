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

    protected FUserInfo testUser;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    protected String customToken;

    @BeforeEach
    public void BaseSetUp() throws Exception {
        testUser = fUserInfoDataRepository.findById("7PfdwpJQqOgINy8XBWQbkqWBlqr1").get();
        customToken = FirebaseAuth.getInstance().createCustomToken(testUser.getUid());
    }

    protected String getTestUserToken(){
        return "Bearer " + customToken;
    }


}
