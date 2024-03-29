package com.wing.forutona;

import com.google.firebase.auth.FirebaseAuth;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    protected  MockMvc mockMvc;

    @PersistenceContext
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
