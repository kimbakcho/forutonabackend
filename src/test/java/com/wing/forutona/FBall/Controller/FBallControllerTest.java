package com.wing.forutona.FBall.Controller;

import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Service.FBallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class FBallControllerTest extends BaseTest {

    @MockBean
    FBallService fBallService;

    @BeforeEach
    void setUp() {

    }


}