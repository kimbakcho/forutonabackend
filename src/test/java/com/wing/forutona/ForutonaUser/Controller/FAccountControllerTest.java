package com.wing.forutona.ForutonaUser.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Dto.UserPositionUpdateReqDto;
import com.wing.forutona.ForutonaUser.Service.FAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FAccountControllerTest extends BaseTest {

    @MockBean
    FAccountService fAccountService;

    @MockBean
    FFireBaseToken fFireBaseToken;

    @BeforeEach
    void setUp()  {

    }


    @Test
    @DisplayName("call Service")
    void updateFireBaseMessageToken() throws Exception {
        //given
        when(fAccountService.updateFireBaseMessageToken(anyString(),anyString())).thenReturn(1);
        MultiValueMap<String,String> params =  new LinkedMultiValueMap<>();
        params.add("uid","test");
        params.add("token","testtoken");
        //when
        MvcResult mvcResult = mockMvc.perform(put("/v1/ForutonaUser/FireBaseMessageToken")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        //then
        verify(fAccountService).updateFireBaseMessageToken(anyString(),anyString());

    }
}